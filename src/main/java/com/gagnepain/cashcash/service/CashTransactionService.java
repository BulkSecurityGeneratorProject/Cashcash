package com.gagnepain.cashcash.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.CashTransaction;
import com.gagnepain.cashcash.domain.FilterParams;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.repository.CashTransactionRepository;
import com.gagnepain.cashcash.service.validator.CashTransactionValidator;
import com.gagnepain.cashcash.service.validator.FilterParamsValidator;
import com.gagnepain.cashcash.web.rest.errors.CashError;
import io.jsonwebtoken.lang.Collections;

/**
 * Service Implementation for managing CashTransaction.
 */
@Service
@Transactional
public class CashTransactionService extends AbstractCashResourceService<CashTransaction> {
	@Inject
	private CashTransactionRepository cashTransactionRepository;

	@Inject
	private CashTransactionValidator cashTransactionValidator;

	@Inject
	private FilterParamsValidator filterParamsValidator;

	@Inject
	private CashSplitSumService cashSplitSumService;

	@PersistenceContext
	private EntityManager entityManager;

	public List<CashTransaction> create(final List<CashTransaction> cashTransactionList) throws BusinessException {
		reconnectUser(cashTransactionList);
		final Optional<CashError> errorOpt = cashTransactionValidator.validateForCreation(cashTransactionList);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		copyTransactionInfoIntoSplit(cashTransactionList);
		List<CashTransaction> saved = cashTransactionRepository.save(cashTransactionList);
		cashSplitSumService.updateAfterCreation(cashTransactionList);
		return saved;
	}

	public CashTransaction update(final CashTransaction cashTransaction) throws BusinessException {
		reconnectUser(cashTransaction);
		final Optional<CashError> errorOpt = cashTransactionValidator.validateForUpdate(cashTransaction);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}
		final CashTransaction oldCashTransaction = cashTransactionRepository.findOneByIdAndUser(cashTransaction.getId(),
				cashTransaction.getUser());
		// We have to detach the entity to keep the old value
		entityManager.detach(oldCashTransaction);
		copyTransactionInfoIntoSplit(cashTransaction);
		CashTransaction updated = cashTransactionRepository.save(cashTransaction);
		cashSplitSumService.updateAfterUpdate(cashTransaction, oldCashTransaction);
		return updated;
	}

	/**
	 * Get all the cashTransactions.
	 *
	 * @param pageable
	 * 		the pagination information
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<CashTransaction> findAll(final Pageable pageable, final FilterParams filterParams) throws BusinessException {
		final Optional<CashError> errorOpt = filterParamsValidator.validate(filterParams);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		final ZonedDateTime endDate;
		if (filterParams.getEndDate() == null) {
			endDate = ZonedDateTime.now()
					.plusDays(1);
		} else {
			endDate = filterParams.getEndDate();
		}

		final ZonedDateTime startDate;
		if (filterParams.getStartDate() == null || filterParams.getStartDate()
				.isAfter(endDate)) {
			startDate = endDate.minusYears(30);
		} else {
			startDate = filterParams.getStartDate();
		}

		final User loggedUser = userService.getUserWithoutAuthorities();
		if (Collections.isEmpty(filterParams.getAccountIdList())) {
			return cashTransactionRepository.findByUserAndTransactionDateBetween(loggedUser, startDate, endDate, pageable);
		} else {
			return cashTransactionRepository.findByUserAndTransactionDateBetweenAndSplitsAccountIdIn(loggedUser, startDate, endDate,
					filterParams.getAccountIdList(), pageable);
		}
	}

	@Transactional(readOnly = true)
	public List<CashTransaction> findFreshCashTransaction() {
		final User loggedUser = userService.getUserWithoutAuthorities();
		return cashTransactionRepository.findTop200ByUserOrderByTransactionDateDesc(loggedUser);
	}


	/**
	 * Get one cashTransaction by id.
	 *
	 * @param id
	 * 		the id of the entity
	 *
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CashTransaction findOne(final Long id) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashTransaction cashTransaction = cashTransactionRepository.findOneByIdAndUser(id, user);

		// We first have to retrieve it in order to validate the retrieved resource
		final Optional<CashError> errorOpt = cashTransactionValidator.validateForRetrieve(cashTransaction);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}
		return cashTransaction;
	}

	/**
	 * Delete the  cashTransaction by id.
	 *
	 * @param id
	 * 		the id of the entity
	 */
	public void delete(final Long id) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashTransaction cashTransaction = cashTransactionRepository.findOneByIdAndUser(id, user);

		// We first have to retrieve it in order to validate the retrieved resource
		final Optional<CashError> errorOpt = cashTransactionValidator.validateForRetrieve(cashTransaction);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		cashTransactionRepository.delete(id);
		cashSplitSumService.updateAfterDeletion(cashTransaction);
	}

	private void copyTransactionInfoIntoSplit(final List<CashTransaction> cashTransactionList) {
		for (final CashTransaction cashTransaction : cashTransactionList) {
			copyTransactionInfoIntoSplit(cashTransaction);
		}
	}

	private void copyTransactionInfoIntoSplit(final CashTransaction cashTransaction) {
		for (final CashSplit split : cashTransaction.getSplits()) {
			split.setTransaction(cashTransaction);
			split.setTransactionDate(cashTransaction.getTransactionDate());
			split.setTransactionDescription(cashTransaction.getDescription());
			split.setTransactionType(cashTransaction.getType());
		}
	}
}
