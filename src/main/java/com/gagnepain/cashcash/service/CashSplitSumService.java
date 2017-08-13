package com.gagnepain.cashcash.service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.map.HashedMap;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.AccountCurrencyKey;
import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.CashSplitSum;
import com.gagnepain.cashcash.domain.CashTransaction;
import com.gagnepain.cashcash.domain.FilterParams;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.repository.CashSplitSumRepository;

/**
 * Service Implementation for managing CashSplitSum.
 */
@Service
@Transactional
public class CashSplitSumService extends AbstractCashResourceService<CashSplitSum> {
	@Inject
	private CashSplitSumRepository cashSplitSumRepository;

	@Inject
	private CashSplitService cashSplitService;

	@Inject
	private UserService userService;

	public void updateAfterCreation(final List<CashTransaction> cashTransactionList) {
		final Map<AccountCurrencyKey, BigDecimal> deltaByAccountAndCurrency = new HashedMap<>();

		cashTransactionList.stream()
				.flatMap(t -> t.getSplits()
						.stream())
				.forEach(s -> {
					final AccountCurrencyKey key = new AccountCurrencyKey(s.getAccount()
							.getId(), s.getCurrency()
							.getId());
					final BigDecimal sum = deltaByAccountAndCurrency.getOrDefault(key, BigDecimal.ZERO);
					deltaByAccountAndCurrency.put(key, sum.add(s.getAmount()));
				});

		applyUpdate(deltaByAccountAndCurrency);
	}

	public void updateAfterUpdate(final CashTransaction cashTransaction, final CashTransaction oldCashTransaction) {
		final Map<AccountCurrencyKey, BigDecimal> deltaByAccountAndCurrency = new HashedMap<>();

		cashTransaction.getSplits()
				.forEach(s -> {
					final AccountCurrencyKey key = new AccountCurrencyKey(s.getAccount()
							.getId(), s.getCurrency()
							.getId());
					final BigDecimal sum = deltaByAccountAndCurrency.getOrDefault(key, BigDecimal.ZERO);
					deltaByAccountAndCurrency.put(key, sum.add(s.getAmount()));
				});

		oldCashTransaction.getSplits()
				.forEach(s -> {
					final AccountCurrencyKey key = new AccountCurrencyKey(s.getAccount()
							.getId(), s.getCurrency()
							.getId());
					final BigDecimal sum = deltaByAccountAndCurrency.getOrDefault(key, BigDecimal.ZERO);
					deltaByAccountAndCurrency.put(key, sum.subtract(s.getAmount()));
				});

		applyUpdate(deltaByAccountAndCurrency);
	}

	public void updateAfterDeletion(final CashTransaction cashTransaction) {
		final Map<AccountCurrencyKey, BigDecimal> deltaByAccountAndCurrency = new HashedMap<>();

		cashTransaction.getSplits()
				.forEach(s -> {
					final AccountCurrencyKey key = new AccountCurrencyKey(s.getAccount()
							.getId(), s.getCurrency()
							.getId());
					final BigDecimal sum = deltaByAccountAndCurrency.getOrDefault(key, BigDecimal.ZERO);
					deltaByAccountAndCurrency.put(key, sum.subtract(s.getAmount()));
				});

		applyUpdate(deltaByAccountAndCurrency);
	}

	private void applyUpdate(final Map<AccountCurrencyKey, BigDecimal> deltaByAccountAndCurrency) {
		deltaByAccountAndCurrency.forEach((key, valueToAdd) -> {
			if (!valueToAdd.equals(BigDecimal.ZERO)) {
				applyUpdate(valueToAdd, key.getAccountId(), key.getCurrencyId());
			}
		});
	}

	private void applyUpdate(final BigDecimal valueToAdd, final long cashAccountId, final long cashCurrencyId) {
		final User loggedUser = userService.getUserWithoutAuthorities();
		final int nbUpdatedRow = cashSplitSumRepository.add(loggedUser.getId(), cashAccountId, cashCurrencyId, valueToAdd);
		if (nbUpdatedRow == 0) {
			// The cashSplitSum doesn't exist for this account/currency
			// We have to create it
			final CashAccount cashAccount = new CashAccount();
			cashAccount.setId(cashAccountId);

			final CashCurrency cashCurrency = new CashCurrency();
			cashCurrency.setId(cashCurrencyId);

			final CashSplitSum cashSplitSum = new CashSplitSum();
			cashSplitSum.setUser(loggedUser);
			cashSplitSum.setAccount(cashAccount);
			cashSplitSum.setCurrency(cashCurrency);

			final BigDecimal actualSum = cashSplitService.getSum(cashAccountId, cashCurrencyId);
			cashSplitSum.setAmount(actualSum);
			cashSplitSumRepository.save(cashSplitSum);
		}
	}

	@Transactional(readOnly = true)
	public List<CashSplitSum> findAll(final FilterParams filterParams) {
		final User loggedUser = userService.getUserWithoutAuthorities();

		List<CashSplitSum> todayCashSplitSumList;
		List<Long> accountIdList = filterParams.getAccountIdList();
		if (CollectionUtils.isEmpty(accountIdList)) {
			todayCashSplitSumList = cashSplitSumRepository.findByUser(loggedUser);
		} else {
			todayCashSplitSumList = cashSplitSumRepository.findByUserAndAccountIdIn(loggedUser, accountIdList);
		}

		final ZonedDateTime startDate = filterParams.getStartDate();
		if (startDate != null) {
			final Map<AccountCurrencyKey, CashSplitSum> deltaCashSplitSumMap = cashSplitService.getSum(loggedUser, accountIdList,
					startDate);
			for (final CashSplitSum todayCashSplitSum : todayCashSplitSumList) {
				final AccountCurrencyKey accountCurrencyKey = new AccountCurrencyKey(todayCashSplitSum.getAccountId(),
						todayCashSplitSum.getCurrencyId());

				final CashSplitSum deltaCashSplitSum = deltaCashSplitSumMap.get(accountCurrencyKey);
				if (deltaCashSplitSum != null) {
					todayCashSplitSum.setAmount(todayCashSplitSum.getAmount()
							.subtract(deltaCashSplitSum.getAmount()));
				}
			}
		}
		return todayCashSplitSumList;
	}

	public void reset(final List<Long> accountIdList) {
		final User loggedUser = userService.getUserWithoutAuthorities();
		List<CashSplitSum> todayCashSplitSumList;
		if (CollectionUtils.isEmpty(accountIdList)) {
			todayCashSplitSumList = cashSplitSumRepository.findByUser(loggedUser);
		} else {
			todayCashSplitSumList = cashSplitSumRepository.findByUserAndAccountIdIn(loggedUser, accountIdList);
		}
		cashSplitSumRepository.deleteInBatch(todayCashSplitSumList);

		final Map<AccountCurrencyKey, CashSplitSum> cashSplitSumMap = cashSplitService.getSum(loggedUser, accountIdList, null);

		cashSplitSumRepository.save(cashSplitSumMap.values());
	}
}
