package com.gagnepain.cashcash.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.AccountCurrencyKey;
import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.CashSplitSum;
import com.gagnepain.cashcash.domain.FilterParams;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.domain.util.JSR310DateConverters;
import com.gagnepain.cashcash.repository.CashSplitRepository;
import com.gagnepain.cashcash.service.validator.CashSplitValidator;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Service Implementation for managing CashSplit.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CashSplitService extends AbstractCashResourceService<CashSplit> {
	@Inject
	private CashSplitRepository cashSplitRepository;

	@Inject
	private CashSplitValidator cashSplitValidator;

	@Inject
	private UserService userService;

	/**
	 * Get all the cashSplits.
	 *
	 * @param pageable
	 * 		the pagination information
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<CashSplit> findAll(final Pageable pageable, final FilterParams filterParams) {
		final User loggedUser = userService.getUserWithoutAuthorities();

		final ZonedDateTime endDate;
		if (filterParams.getEndDate() == null) {
			endDate = ZonedDateTime.now();
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

		if (CollectionUtils.isNotEmpty(filterParams.getAccountIdList())) {
			return cashSplitRepository.findByUserAndAccountIdInAndTransactionDateBetween(loggedUser,
					filterParams.getAccountIdList(), startDate, endDate, pageable);
		} else {
			return cashSplitRepository.findByUserAndTransactionDateBetweenOrderByTransactionDateAsc(loggedUser, startDate, endDate,
					pageable);
		}
	}

	/**
	 * Get one cashSplit by id.
	 *
	 * @param id
	 * 		the id of the entity
	 *
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CashSplit findOne(final Long id) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashSplit cashSplit = cashSplitRepository.findOneByIdAndUser(id, user);

		// We first have to retrieve it in order to validate the retrieved resource
		final Optional<CashError> errorOpt = cashSplitValidator.validateForRetrieve(cashSplit);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}
		return cashSplit;
	}

	public BigDecimal getSum(final Long cashAccountId, final Long cashCurrencyId) {
		final Long userId = userService.getUserWithoutAuthorities()
				.getId();
		return cashSplitRepository.getSum(userId, cashAccountId, cashCurrencyId);
	}

	public Map<AccountCurrencyKey, CashSplitSum> getSum(final User loggedUser, final List<Long> accountIdList,
			final ZonedDateTime fromDate) {
		final Map<AccountCurrencyKey, CashSplitSum> cashSplitSumMap = new HashMap<>();

		final Long userId = loggedUser.getId();
		List<Object[]> objectResultList;

		if (CollectionUtils.isEmpty(accountIdList) && fromDate == null) {
			objectResultList = cashSplitRepository.getSum(userId);
		} else if (!CollectionUtils.isEmpty(accountIdList) && fromDate == null) {
			objectResultList = cashSplitRepository.getSum(userId, accountIdList);
		} else if (CollectionUtils.isEmpty(accountIdList) && fromDate != null) {
			objectResultList = cashSplitRepository.getSum(userId,
					JSR310DateConverters.ZonedDateTimeToDateConverter.INSTANCE.convert(fromDate));
		} else {
			objectResultList = cashSplitRepository.getSum(userId, accountIdList,
					JSR310DateConverters.ZonedDateTimeToDateConverter.INSTANCE.convert(fromDate));
		}

		for (final Object[] objectResult : objectResultList) {
			final Long cashAccountId = ((BigInteger) objectResult[0]).longValue();
			final Long cashCurrencyId = ((BigInteger) objectResult[1]).longValue();
			final AccountCurrencyKey accountCurrencyKey = new AccountCurrencyKey(cashAccountId, cashCurrencyId);
			final BigDecimal sum = (BigDecimal) objectResult[2];

			final CashAccount cashAccount = new CashAccount();
			cashAccount.setId(cashAccountId);

			final CashCurrency cashCurrency = new CashCurrency();
			cashCurrency.setId(cashCurrencyId);

			final CashSplitSum cashSplitSum = new CashSplitSum();
			cashSplitSum.setAccount(cashAccount);
			cashSplitSum.setCurrency(cashCurrency);
			cashSplitSum.setAmount(sum);
			cashSplitSum.setUser(loggedUser);

			cashSplitSumMap.put(accountCurrencyKey, cashSplitSum);
		}

		return cashSplitSumMap;
	}
}
