package com.gagnepain.cashcash.service.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.enumeration.CashAccountType;
import com.gagnepain.cashcash.repository.CashAccountRepository;
import com.gagnepain.cashcash.repository.CashSplitRepository;
import com.gagnepain.cashcash.service.UserService;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Class which suppose to validate the cashAccount resource
 */
@Component
public class CashAccountValidator extends AbstractValidator<CashAccount> {
	@Inject
	private CashAccountRepository cashAccountRepository;

	@Inject
	private CashSplitRepository cashSplitRepository;

	@Inject
	private UserService userService;

	@Value("${cashcash.maxCashAccountNb}")
	private int MAX_CASH_ACCOUNT_NB;

	@Value("${cashcash.maxCashAccountLevel}")
	private int MAX_CASH_ACCOUNT_LEVEL;

	public Optional<CashError> validateForCreation(final List<CashAccount> cashAccountList) {
		final User loggedUser = userService.getUserWithoutAuthorities();
		long existingAccountNb = cashAccountRepository.countByUser(loggedUser);

		if (cashAccountList.size() + existingAccountNb > MAX_CASH_ACCOUNT_NB) {
			return Optional.of(
					new CashError("tooMuchCashAccount", "Impossible to have more than " + MAX_CASH_ACCOUNT_NB + " cash accounts."));
		}

		for (final CashAccount cashAccount : cashAccountList) {
			final Optional<CashError> cashError = validateForCreation(cashAccount);
			if (cashError.isPresent()) {
				return cashError;
			}
		}

		return Optional.empty();
	}

	public Optional<CashError> validateForCreation(final CashAccount cashAccount) {
		final Optional<CashError> error = super.validateForCreation(cashAccount);
		if (error.isPresent()) {
			return error;
		}

		if (cashAccount.getLevel() == 1) {
			return Optional.of(new CashError("noCreationRootAccount", "Creating a new root account is not possible"));
		}

		return validateConsistency(cashAccount);
	}

	public Optional<CashError> validateForInitialCreation(final User newUser) {

		final List<CashAccount> cashAccountList = cashAccountRepository.findByUser(newUser);
		if (!cashAccountList.isEmpty()) {
			return Optional.of(new CashError("accountsExist", "This user already has accounts"));
		}

		return Optional.empty();
	}

	public Optional<CashError> validateForUpdate(final List<CashAccount> cashAccountList) {
		if (CollectionUtils.isEmpty(cashAccountList)) {
			return Optional.of(new CashError("emptyList", "The cashAccount list is empty"));
		} else if (cashAccountList.size() == 1) {
			return validateForOneUpdate(cashAccountList.get(0));
		} else {
			return validateForAllUpdate(cashAccountList);
		}
	}

	public Optional<CashError> validateForDeletion(final CashAccount cashAccount) {
		final Optional<CashError> error = super.validateForDeletion(cashAccount);
		if (error.isPresent()) {
			return error;
		}

		final List<CashAccount> childrenCashAccounts = cashAccountRepository.findByUserAndParentAccount(cashAccount.getUser(), cashAccount);
		if (CollectionUtils.isNotEmpty(childrenCashAccounts)) {
			return Optional.of(new CashError("childrenAccountsExist", "Impossible to delete an account with children accounts"));
		}

		final CashSplit cashSplit = cashSplitRepository.findFirstByAccount(cashAccount);
		if (cashSplit != null) {
			return Optional.of(new CashError("transactionsExist", "Impossible to delete an account with transactions"));
		}

		//No error
		return Optional.empty();
	}

	protected Optional<CashError> validateForUpdate(final CashAccount cashAccount, final CashAccount oldCashAccount) {
		final Optional<CashError> error = super.validateForUpdate(cashAccount, oldCashAccount);
		if (error.isPresent()) {
			return error;
		}

		if (cashAccount.getLevel() > 1 && cashAccount.getParentAccount() == null) {
			return Optional.of(new CashError("incorrectLevel", "level superior to 1 with no parent account"));
		}

		if (oldCashAccount.getParentAccount() != null) {
			if (!Objects.equals(cashAccount.getParentAccount(), oldCashAccount.getParentAccount())) {
				return Optional.of(new CashError("incorrectRootAccount", "rootAccount parentId can't be changed"));
			}
			if (!StringUtils.equals(cashAccount.getName(), oldCashAccount.getName())) {
				return Optional.of(new CashError("incorrectRootAccount", "rootAccount name can't be changed"));
			}
			if (cashAccount.getType() != oldCashAccount.getType()) {
				return Optional.of(new CashError("incorrectRootAccount", "rootAccount type can't be changed"));
			}
		}

		return Optional.empty();
	}

	private Optional<CashError> validateForAllUpdate(final List<CashAccount> cashAccountList) {
		final List<CashAccount> cashAccountInDBList = cashAccountRepository.findByUser(cashAccountList.get(0)
				.getUser());

		if (cashAccountList.size() != cashAccountInDBList.size()) {
			return Optional.of(new CashError("invalidNumberOfAccount",
					"The number of Account in the database differ from the list of accounts to update"));
		}

		final Map<Long, CashAccount> mapOfCashAccountInDBList = cashAccountInDBList.stream()
				.collect(Collectors.toMap(CashAccount::getId, c -> c));
		final Map<Long, CashAccount> mapOfCashAccount = new HashMap<>();

		for (final CashAccount cashAccount : cashAccountList) {
			final Long id = cashAccount.getId();
			if (!mapOfCashAccountInDBList.containsKey(id)) {
				return Optional.of(new CashError("unknownId", "An account doesn't exist in the database"));
			}
			if (mapOfCashAccount.containsKey(id)) {
				return Optional.of(new CashError("duplicateId", "Several cashAccount have identical ids"));
			}
			if (cashAccount.getLevel() == 1) {
				CashAccount cashAccountInDB = mapOfCashAccountInDBList.get(cashAccount.getId());
				if (cashAccountInDB.getLevel() != 1) {
					return Optional.of(new CashError("impossibleToUpdateRootAccount", "Updating root account is not allowed"));
				}
			}
			mapOfCashAccount.put(id, cashAccount);
		}

		final ArrayList<CashAccount> rootCashAccountList = new ArrayList<>();

		for (final CashAccount cashAccount : cashAccountList) {
			if (cashAccount.getParentAccount() == null) {
				rootCashAccountList.add(cashAccount);
			}
			final Optional<CashError> error = validateConsistency(cashAccount);
			if (error.isPresent()) {
				return error;
			}
		}

		if (rootCashAccountList.size() != CashAccountType.values().length) {
			return Optional.of(new CashError("incorrectRootAccounts", "Number of rootAccount can't be changed"));
		}

		return Optional.empty();
	}

	private Optional<CashError> validateForOneUpdate(final CashAccount cashAccount) {
		final User loggedUser = userService.getUserWithoutAuthorities();
		final CashAccount cashAccountInDB = findResourceByUserAndId(loggedUser, cashAccount.getId());
		super.validateForUpdate(cashAccount, cashAccountInDB);

		if (!Objects.equals(cashAccount.getParentAccount()
				.getId(), cashAccountInDB.getParentAccount()
				.getId())) {
			return Optional.of(
					new CashError("impossibleToChangeParentAccount", "Changing parent account with single update is " + "forbidden"));
		}

		if (cashAccount.getParentAccount() == null) {
			return Optional.of(new CashError("rootAccountNoModification", "Root Account can't be changed"));
		} else {
			final Optional<CashError> error = validateConsistency(cashAccount);
			if (error.isPresent()) {
				return error;
			}
		}
		return Optional.empty();
	}

	private Optional<CashError> validateConsistency(final CashAccount cashAccount) {
		final CashAccount parentAccount = cashAccount.getParentAccount();
		if (parentAccount == null) {
			if (cashAccount.getLevel() != 1) {
				return Optional.of(new CashError("inconsistentLevel", "Account level of the root account must be 1"));
			}
		} else {
			if (cashAccount.getLevel() < 1) {
				return Optional.of(new CashError("incorrectLevel", "Account level must be greater or equal to 1"));
			}
			if(cashAccount.getLevel() > MAX_CASH_ACCOUNT_LEVEL){
				return Optional.of(new CashError("tooMuchLevel", "Impossible to create account with a level greater than "+ MAX_CASH_ACCOUNT_LEVEL));
			}
			if (cashAccount.getLevel() != parentAccount.getLevel() + 1) {
				return Optional.of(new CashError("inconsistentLevel", "Account level is not consistent with the parent account level"));
			}
			if (parentAccount.getType() != cashAccount.getType()) {
				return Optional.of(new CashError("inconsistentType", "Account type differ from the parent account type"));
			}
		}
		return Optional.empty();
	}

	@Override
	CashAccount findResourceByUserAndId(final User user, final Long id) {
		return cashAccountRepository.findOneByIdAndUser(id, user);
	}
}
