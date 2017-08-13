package com.gagnepain.cashcash.service;

import java.util.Locale;
import javax.inject.Inject;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import com.gagnepain.cashcash.config.JHipsterProperties;
import com.gagnepain.cashcash.domain.User;

/**
 * Service for sending e-mails.
 * <p>
 * We use the @Async annotation to send e-mails asynchronously.
 * </p>
 */
@Service
public class MailService {
	private final Logger log = LoggerFactory.getLogger(MailService.class);

	private static final String USER = "user";

	private static final String BASE_URL = "baseUrl";

	@Inject
	private JHipsterProperties jHipsterProperties;

	@Inject
	private JavaMailSenderImpl javaMailSender;

	@Inject
	private MessageSource messageSource;

	@Inject
	private SpringTemplateEngine templateEngine;

	@Async
	public void sendEmail(final String to, final String subject, final String content, final boolean isMultipart, final boolean isHtml) {
		log.debug("Send e-mail[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart, isHtml, to, subject,
				content);

		// Prepare message using a Spring helper
		final MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		try {
			final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
			message.setTo(to);
			message.setFrom(jHipsterProperties.getMail()
					.getFrom());
			message.setSubject(subject);
			message.setText(content, isHtml);
			javaMailSender.send(mimeMessage);
			log.debug("Sent e-mail to User '{}'", to);
		} catch (final Exception e) {
			log.warn("E-mail could not be sent to user '{}', exception is: {}", to, e.getMessage());
		}
	}

	@Async
	public void sendActivationEmail(final User user, final String baseUrl) {
		log.debug("Sending activation e-mail to '{}'", user.getEmail());
		final Locale locale = Locale.forLanguageTag(user.getLangKey());
		final Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(BASE_URL, baseUrl);
		final String content = templateEngine.process("activationEmail", context);
		final String subject = messageSource.getMessage("email.activation.title", null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Async
	public void sendCreationEmail(final User user, final String baseUrl) {
		log.debug("Sending creation e-mail to '{}'", user.getEmail());
		final Locale locale = Locale.forLanguageTag(user.getLangKey());
		final Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(BASE_URL, baseUrl);
		final String content = templateEngine.process("creationEmail", context);
		final String subject = messageSource.getMessage("email.activation.title", null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Async
	public void sendPasswordResetMail(final User user, final String baseUrl) {
		log.debug("Sending password reset e-mail to '{}'", user.getEmail());
		final Locale locale = Locale.forLanguageTag(user.getLangKey());
		final Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable(BASE_URL, baseUrl);
		final String content = templateEngine.process("passwordResetEmail", context);
		final String subject = messageSource.getMessage("email.reset.title", null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);
	}

	@Async
	public void sendSocialRegistrationValidationEmail(final User user, final String provider) {
		log.debug("Sending social registration validation e-mail to '{}'", user.getEmail());
		final Locale locale = Locale.forLanguageTag(user.getLangKey());
		final Context context = new Context(locale);
		context.setVariable(USER, user);
		context.setVariable("provider", WordUtils.capitalize(provider));
		final String content = templateEngine.process("socialRegistrationValidationEmail", context);
		final String subject = messageSource.getMessage("email.social.registration.title", null, locale);
		sendEmail(user.getEmail(), subject, content, false, true);
	}
}
