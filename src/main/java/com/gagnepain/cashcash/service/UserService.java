package com.gagnepain.cashcash.service;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.Authority;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.repository.AuthorityRepository;
import com.gagnepain.cashcash.repository.UserRepository;
import com.gagnepain.cashcash.security.AuthoritiesConstants;
import com.gagnepain.cashcash.security.SecurityUtils;
import com.gagnepain.cashcash.service.util.RandomUtil;
import com.gagnepain.cashcash.web.rest.dto.ManagedUserDTO;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {
	private final Logger log = LoggerFactory.getLogger(UserService.class);

	@Inject
	private SocialService socialService;

	@Inject
	private PasswordEncoder passwordEncoder;

	@Inject
	private UserRepository userRepository;

	@Inject
	private AuthorityRepository authorityRepository;

	public Optional<User> activateRegistration(final String key) {
		log.debug("Activating user for activation key {}", key);
		return userRepository.findOneByActivationKey(key)
				.map(user -> {
					// activate given user for the registration key.
					user.setActivated(true);
					user.setActivationKey(null);
					userRepository.save(user);
					log.debug("Activated user: {}", user);
					return user;
				});
	}

	public Optional<User> completePasswordReset(final String newPassword, final String key) {
		log.debug("Reset user password for reset key {}", key);

		return userRepository.findOneByResetKey(key)
				.filter(user -> {
					ZonedDateTime oneDayAgo = ZonedDateTime.now()
							.minusHours(24);
					return user.getResetDate()
							.isAfter(oneDayAgo);
				})
				.map(user -> {
					user.setPassword(passwordEncoder.encode(newPassword));
					user.setResetKey(null);
					user.setResetDate(null);
					userRepository.save(user);
					return user;
				});
	}

	public Optional<User> requestPasswordReset(final String mail) {
		return userRepository.findOneByEmail(mail)
				.filter(User::getActivated)
				.map(user -> {
					user.setResetKey(RandomUtil.generateResetKey());
					user.setResetDate(ZonedDateTime.now());
					userRepository.save(user);
					return user;
				});
	}

	public User createUserInformation(final String login, final String password, final String firstName, final String lastName,
			final String email, final String langKey) {

		final User newUser = new User();
		final Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
		final Set<Authority> authorities = new HashSet<>();
		final String encryptedPassword = passwordEncoder.encode(password);
		newUser.setLogin(login);
		// new user gets initially a generated password
		newUser.setPassword(encryptedPassword);
		newUser.setFirstName(firstName);
		newUser.setLastName(lastName);
		newUser.setEmail(email);
		newUser.setLangKey(langKey);
		// new user is not active
		newUser.setActivated(false);
		// new user gets registration key
		newUser.setActivationKey(RandomUtil.generateActivationKey());
		authorities.add(authority);
		newUser.setAuthorities(authorities);
		userRepository.save(newUser);
		log.debug("Created Information for User: {}", newUser);
		return newUser;
	}

	public User createUser(final ManagedUserDTO managedUserDTO) {
		final User user = new User();
		user.setLogin(managedUserDTO.getLogin());
		user.setFirstName(managedUserDTO.getFirstName());
		user.setLastName(managedUserDTO.getLastName());
		user.setEmail(managedUserDTO.getEmail());
		if (managedUserDTO.getLangKey() == null) {
			user.setLangKey("en"); // default language
		} else {
			user.setLangKey(managedUserDTO.getLangKey());
		}
		if (managedUserDTO.getAuthorities() != null) {
			final Set<Authority> authorities = new HashSet<>();
			managedUserDTO.getAuthorities()
					.stream()
					.forEach(authority -> authorities.add(authorityRepository.findOne(authority)));
			user.setAuthorities(authorities);
		}
		final String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
		user.setPassword(encryptedPassword);
		user.setResetKey(RandomUtil.generateResetKey());
		user.setResetDate(ZonedDateTime.now());
		user.setActivated(true);
		userRepository.save(user);
		log.debug("Created Information for User: {}", user);
		return user;
	}

	public void updateUserInformation(final String firstName, final String lastName, final String email, final String langKey) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
				.ifPresent(u -> {
					u.setFirstName(firstName);
					u.setLastName(lastName);
					u.setEmail(email);
					u.setLangKey(langKey);
					userRepository.save(u);
					log.debug("Changed Information for User: {}", u);
				});
	}

	public void deleteUserInformation(final String login) {
		userRepository.findOneByLogin(login)
				.ifPresent(u -> {
					socialService.deleteUserSocialConnection(u.getLogin());
					userRepository.delete(u);
					log.debug("Deleted User: {}", u);
				});
	}

	public void changePassword(final String password) {
		userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
				.ifPresent(u -> {
					final String encryptedPassword = passwordEncoder.encode(password);
					u.setPassword(encryptedPassword);
					userRepository.save(u);
					log.debug("Changed password for User: {}", u);
				});
	}

	@Transactional(readOnly = true)
	public Optional<User> getUserWithAuthoritiesByLogin(final String login) {
		return userRepository.findOneByLogin(login)
				.map(u -> {
					u.getAuthorities()
							.size();
					return u;
				});
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities(final Long id) {
		final User user = userRepository.findOne(id);
		user.getAuthorities()
				.size(); // eagerly load the association
		return user;
	}

	@Transactional(readOnly = true)
	public User getUserWithAuthorities() {
		final User user = getUserWithoutAuthorities();
		user.getAuthorities()
				.size(); // eagerly load the association
		return user;
	}

	@Transactional(readOnly = true)
	public User getUserWithoutAuthorities() {
		final User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin())
				.get();
		return user;
	}

	/**
	 * Not activated users should be automatically deleted after 3 days.
	 * <p>
	 * This is scheduled to get fired everyday, at 01:00 (am).
	 * </p>
	 */
	@Scheduled(cron = "0 0 1 * * ?")
	public void removeNotActivatedUsers() {
		final ZonedDateTime now = ZonedDateTime.now();
		final List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
		for (final User user : users) {
			log.debug("Deleting not activated user {}", user.getLogin());
			userRepository.delete(user);
		}
	}
}
