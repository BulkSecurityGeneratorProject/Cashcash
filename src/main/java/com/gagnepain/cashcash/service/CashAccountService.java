package com.gagnepain.cashcash.service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.joda.money.CurrencyUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.repository.CashAccountRepository;
import com.gagnepain.cashcash.service.util.CashAccountGenerator;
import com.gagnepain.cashcash.service.validator.CashAccountValidator;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Service Implementation for managing CashAccount.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CashAccountService extends AbstractCashResourceService<CashAccount> {
	@Inject
	private CashAccountRepository cashAccountRepository;

	@Inject
	private CashAccountValidator cashAccountValidator;

	@Inject
	private CashCurrencyService cashCurrencyService;

	/**
	 * Create a new cashAccount
	 */
	public List<CashAccount> create(final List<CashAccount> cashAccountList) throws BusinessException {
		reconnectUser(cashAccountList);
		final List<CashAccount> cashAccountsToSave = reconnectWithExistingResources(cashAccountList);
		final Optional<CashError> errorOpt = cashAccountValidator.validateForCreation(cashAccountsToSave);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}
		return cashAccountRepository.save(cashAccountsToSave);
	}

	/**
	 * Update cashAccounts
	 */
	public List<CashAccount> update(final List<CashAccount> cashAccountList) throws BusinessException {
		reconnectUser(cashAccountList);
		final Optional<CashError> errorOpt = cashAccountValidator.validateForUpdate(cashAccountList);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}
		return cashAccountRepository.save(cashAccountList);
	}

	/**
	 * Get all the cashAccounts.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<CashAccount> findAll() {
		final User loggedUser = userService.getUserWithoutAuthorities();
		final List<CashAccount> cashAccountList = cashAccountRepository.findByUser(loggedUser);

		cashAccountList.sort((acc1, acc2) -> {
			final int compareResult = Integer.compare(acc1.getLevel(), acc2.getLevel());
			if (compareResult != 0) {
				return compareResult;
			} else {
				return acc1.getName()
						.compareTo(acc2.getName());
			}
		});

		return cashAccountList;
	}

	/**
	 * Get one cashAccount by id.
	 *
	 * @param id
	 * 		the id of the entity
	 *
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CashAccount findOne(final Long id) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashAccount cashAccount = cashAccountRepository.findOneByIdAndUser(id, user);

		// We first have to retrieve it in order to validate the retrieved resource
		final Optional<CashError> errorOpt = cashAccountValidator.validateForRetrieve(cashAccount);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		return cashAccount;
	}

	/**
	 * Get one cashAccount by accountNumber.
	 *
	 * @param accountNumber
	 * 		the accountNumber of the entity
	 *
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CashAccount findOneByAccountNumber(final String accountNumber) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashAccount cashAccount = cashAccountRepository.findByUserAndAccountNumber(user, accountNumber);

		// We first have to retrieve it in order to validate the retrieved resource
		final Optional<CashError> errorOpt = cashAccountValidator.validateForRetrieve(cashAccount);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		return cashAccount;
	}

	/**
	 * Delete the  cashAccount by id.
	 *
	 * @param id
	 * 		the id of the entity
	 */
	public void delete(final Long id) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashAccount cashAccount = cashAccountRepository.findOneByIdAndUser(id, user);

		// We first have to retrieve it in order to validate the deletion of the resource
		final Optional<CashError> errorOpt = cashAccountValidator.validateForDeletion(cashAccount);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		cashAccountRepository.delete(id);
	}

	/**
	 * Create initial cashAccount list
	 */
	public List<CashAccount> createInitialCashAccount() throws BusinessException {
		final User loggedUser = userService.getUserWithoutAuthorities();
		final Optional<CashError> errorOpt = cashAccountValidator.validateForInitialCreation(loggedUser);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		// Create the initial currency
		final List<CashCurrency> allCurrency = cashCurrencyService.findAll();
		if (allCurrency.isEmpty()) {
			cashCurrencyService.createCurrencies();
		}
		final CashCurrency cashCurrency = cashCurrencyService.findOne(CurrencyUnit.EUR.getCode());

		final Collection<CashAccount> cashAccountList = CashAccountGenerator.generateInitialAccounts(loggedUser, cashCurrency);
		return cashAccountRepository.save(cashAccountList);
	}

	private List<CashAccount> reconnectWithExistingResources(final List<CashAccount> cashAccountList) {
		final List<CashAccount> cashAccountInDB = cashAccountRepository.findByUserIsCurrentUser();
		final Map<String, CashAccount> cashAccountInDBMap = cashAccountInDB.stream()
				.collect(Collectors.toMap(CashAccount::getName, Function.identity()));

		final List<CashCurrency> cashCurrenciesInDB = cashCurrencyService.findAll();
		final Map<String, CashCurrency> cashCurrencyInDBMap = cashCurrenciesInDB.stream()
				.collect(Collectors.toMap(CashCurrency::getCode, Function.identity()));

		final Map<String, CashAccount> cashAccountToSaveMap = cashAccountList.stream()
				.collect(Collectors.toMap(CashAccount::getName, Function.identity()));

		final List<CashAccount> connectedCashAccounts = cashAccountList.stream()
				.filter(cashAccount -> cashAccountInDBMap.get(cashAccount.getName()) == null)
				.map(cashAccount -> {
					cashAccount.setId(null);
					return cashAccount;
				})
				.map(cashAccount -> {
					CashCurrency currency = cashCurrencyInDBMap.get(cashAccount.getCurrency()
							.getCode());
					cashAccount.setCurrency(currency);
					return cashAccount;
				})
				.map(cashAccount -> {
					CashAccount parentAccount = cashAccount.getParentAccount();
					if (parentAccount != null && cashAccountInDBMap.get(parentAccount.getName()) != null) {
						cashAccount.setParentAccount(cashAccountInDBMap.get(parentAccount.getName()));
					} else if (parentAccount != null && cashAccountToSaveMap.get(parentAccount.getName()) != null) {
						cashAccount.setParentAccount(cashAccountToSaveMap.get(parentAccount.getName()));
					}
					return cashAccount;
				})
				.collect(Collectors.toList());

		return connectedCashAccounts;
	}
}
