package com.gagnepain.cashcash.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.CashSplitCumulative;
import com.gagnepain.cashcash.domain.CashSplitSum;
import com.gagnepain.cashcash.domain.FilterParams;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.repository.CashSplitSumRepository;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Service Implementation for managing CashSplitCumulative.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CashSplitCumulativeService {
	private final Logger log = LoggerFactory.getLogger(CashSplitCumulativeService.class);

	@Inject
	private CashSplitSumRepository cashSplitSumRepository;

	@Inject
	private CashSplitService cashSplitService;

	@Inject
	private CashCurrencyService cashCurrencyService;

	@Inject
	private CashConverterService cashConverterService;

	@Inject
	private UserService userService;

	@Value("${cashcash.maxPageSize}")
	private int MAX_PAGE_SIZE;

	@Transactional(readOnly = true)
	public List<CashSplitCumulative> findAll(final FilterParams filterParams) throws BusinessException {
		String currencyCode;
		if (null != filterParams.getCurrencyCode()) {
			currencyCode = filterParams.getCurrencyCode();
		} else {
			currencyCode = "EUR";
		}
		CashCurrency expectedCurrency = cashCurrencyService.findOne(currencyCode);
		if (null == expectedCurrency) {
			final CashError cashError = new CashError("unknownCurrency", "The provided currency is unknown");
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		final User loggedUser = userService.getUserWithoutAuthorities();

		// To build the cumulative sum, we need:
		// - All the cash split recorded starting from the start date (ordered with most recent first)
		// - The present cash split sum

		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "transactionDate"));
		Pageable pageable = new PageRequest(0, MAX_PAGE_SIZE, sort);
		final Page<CashSplit> cashSplitList = cashSplitService.findAll(pageable, filterParams);

		if (cashSplitList.getTotalPages() > 1) {
			final CashError cashError = new CashError("tooBigRange", "The range is too big");
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		List<CashSplitSum> todayCashSplitSumList;
		List<Long> accountIdList = filterParams.getAccountIdList();
		if (CollectionUtils.isEmpty(accountIdList)) {
			todayCashSplitSumList = cashSplitSumRepository.findByUser(loggedUser);
		} else {
			todayCashSplitSumList = cashSplitSumRepository.findByUserAndAccountIdIn(loggedUser, accountIdList);
		}

		Map<CashAccount, BigDecimal> nextSumMap = new HashMap<>();
		for (CashSplitSum todayCashSplitSum : todayCashSplitSumList) {
			BigDecimal sum = cashConverterService.convert(todayCashSplitSum.getAmount(), todayCashSplitSum.getCurrency()
					.getCode(), expectedCurrency.getCode());
			BigDecimal existingSum = nextSumMap.get(todayCashSplitSum.getAccount());
			if (null == existingSum) {
				nextSumMap.put(todayCashSplitSum.getAccount(), sum);
			} else {
				nextSumMap.put(todayCashSplitSum.getAccount(), sum.add(existingSum));
			}
		}

		// We will now go through all the cashSplit to do a cumulative substraction on each one
		List<CashSplitCumulative> cashSplitCumulativeList = cashSplitList.getContent()
				.stream()
				.map(currentCashSplit -> {
					// We retrieve the current sum
					BigDecimal currentSum = nextSumMap.get(currentCashSplit.getAccount());

					BigDecimal cashSplitAmount = null;
					try {
						cashSplitAmount = cashConverterService.convert(currentCashSplit.getAmount(), currentCashSplit.getCurrency()
								.getCode(), expectedCurrency.getCode());
						// We update the sum for the next cashsplit
						nextSumMap.put(currentCashSplit.getAccount(), currentSum.subtract(cashSplitAmount));
					} catch (BusinessException e) {
						log.error("conversionError", e);
					}

					return new CashSplitCumulative(currentCashSplit, currentSum, expectedCurrency);
				})
				.filter(currentCashSplitCumulative -> {
					ZonedDateTime endDateParam = filterParams.getEndDate();
					return null == endDateParam || endDateParam.isAfter(currentCashSplitCumulative.getTransactionDate());
				})
				.collect(Collectors.toList());

		return cashSplitCumulativeList;
	}
}
