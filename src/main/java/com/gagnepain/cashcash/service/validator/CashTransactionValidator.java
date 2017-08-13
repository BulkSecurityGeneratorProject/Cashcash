package com.gagnepain.cashcash.service.validator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.CashTransaction;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.repository.CashTransactionRepository;
import com.gagnepain.cashcash.web.rest.errors.CashError;
import com.google.common.base.Objects;

/**
 * Class which suppose to validate the cashTransaction resource
 */
@Component
public class CashTransactionValidator extends AbstractValidator<CashTransaction> {
	@Inject
	private CashTransactionRepository cashTransactionRepository;

	@Value("${cashcash.maxSplitPerTransaction}")
	private int MAX_SPLIT_NB_PER_TRANSACTION;

	@Value("${cashcash.maxCashTransactionCreation}")
	private int MAX_CASH_TRANSACTION_CREATION;

	public Optional<CashError> validateForCreation(final List<CashTransaction> cashTransactionList) {
		if (CollectionUtils.isEmpty(cashTransactionList)) {
			return Optional.of(new CashError("emptyList", "The cashTransaction list is empty"));
		}

		if (cashTransactionList.size() > MAX_CASH_TRANSACTION_CREATION) {
			return Optional.of(new CashError("impossibleToCreateAsMuchTransaction", "Impossible to create that much transactions."));
		}

		for (final CashTransaction cashTransaction : cashTransactionList) {
			final Optional<CashError> error = validateForCreation(cashTransaction);
			if (error.isPresent()) {
				return error;
			}
		}

		return Optional.empty();
	}

	public Optional<CashError> validateForCreation(final CashTransaction cashTransaction) {
		final Optional<CashError> cashError = super.validateForCreation(cashTransaction);
		if (cashError.isPresent()) {
			return cashError;
		}

		final Optional<CashError> error = commonValidate(cashTransaction);
		if (error.isPresent()) {
			return error;
		}

		return Optional.empty();
	}

	public Optional<CashError> validateForUpdate(final CashTransaction cashTransaction) {
		final Optional<CashError> cashError = super.validateForUpdate(cashTransaction);
		if (cashError.isPresent()) {
			return cashError;
		}

		final Optional<CashError> error = commonValidate(cashTransaction);
		if (error.isPresent()) {
			return error;
		}

		return Optional.empty();
	}

	@Override
	CashTransaction findResourceByUserAndId(final User user, final Long id) {
		return cashTransactionRepository.findOneByIdAndUser(id, user);
	}

	private Optional<CashError> commonValidate(final CashTransaction cashTransaction) {
		BigDecimal total = BigDecimal.ZERO;
		final List<CashAccount> accounts = new ArrayList<>();

		if (cashTransaction.getSplits()
				.size() % 2 != 0) {
			return Optional.of(new CashError("oddSplitNumber", "The number of splits is incorrect."));
		}

		if (cashTransaction.getSplits()
				.size() > MAX_SPLIT_NB_PER_TRANSACTION) {
			return Optional.of(new CashError("tooMuchSplitPerTransaction", "The number of splits is incorrect."));
		}

		for (final CashSplit split : cashTransaction.getSplits()) {
			if (accounts.contains(split.getAccount())) {
				return Optional.of(new CashError("identicalInAndOutAccount", "In and Out accounts can't be identicals."));
			} else {
				accounts.add(split.getAccount());
			}

			if (!Objects.equal(cashTransaction.getUser(), split.getUser())) {
				return Optional.of(
						new CashError("noneIdenticalUserId", "The userId from transaction is different from the userId from account"));
			}

			total = total.add(split.getAmount());
		}

		if (total.compareTo(BigDecimal.ZERO) != 0) {
			return Optional.of(new CashError("noneBalanceTransaction", "The transaction is not balanced"));
		}

		//No error
		return Optional.empty();
	}
}
