package com.gagnepain.cashcash.service.fileservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.CashTransaction;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.service.CashAccountService;
import com.gagnepain.cashcash.service.CashCurrencyService;
import com.gagnepain.cashcash.service.CashTransactionService;
import com.gagnepain.cashcash.service.UserService;
import com.gagnepain.cashcash.service.fileservice.mapper.CashTransactionToImport;

/**
 * Service class for ofx file
 */
@Service
@Transactional
public class JsonFileService {
	@Inject
	CashAccountService cashAccountService;

	@Inject
	CashCurrencyService cashCurrencyService;

	@Inject
	private UserService userService;

	@Inject
	private CashTransactionService cashTransactionService;

	@Inject
	private Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder;

	private final Logger log = LoggerFactory.getLogger(JsonFileService.class);

	public List<CashTransactionToImport> extractTransaction(final InputStream inputStream) throws IOException, BusinessException {
		final ObjectMapper objectMapper = new ObjectMapper();
		jackson2ObjectMapperBuilder.configure(objectMapper);
		final CashTransactionToImport[] cashTransactionToImportList = objectMapper.readValue(inputStream, CashTransactionToImport[].class);

		final List<CashAccount> cashAccountInDB = cashAccountService.findAll();
		final Map<String, CashAccount> cashAccountInDBMap = cashAccountInDB.stream()
				.collect(Collectors.toMap(CashAccount::getName, Function.identity()));

		final List<CashCurrency> cashCurrenciesInDB = cashCurrencyService.findAll();
		final Map<String, CashCurrency> cashCurrencyInDBMap = cashCurrenciesInDB.stream()
				.collect(Collectors.toMap(CashCurrency::getCode, Function.identity()));

		final User user = userService.getUserWithoutAuthorities();

		final List<CashTransaction> connectedCashTransactions = Arrays.stream(cashTransactionToImportList)
				.map(CashTransaction::new)
				.map(t -> {
					t.setId(null);
					return t;
				})
				.map(t -> {
					t.setUser(user);
					return t;
				})
				.map(t -> {
					updateSplit(t.getSplits(), cashAccountInDBMap, cashCurrencyInDBMap, user);
					return t;
				})
				.collect(Collectors.toList());

		if (connectedCashTransactions.size() > 50) {
			cashTransactionService.create(connectedCashTransactions);
			return Collections.EMPTY_LIST;
		}
		return Arrays.asList(cashTransactionToImportList);
	}

	private void updateSplit(final List<CashSplit> splits, final Map<String, CashAccount> cashAccountInDBMap,
			final Map<String, CashCurrency> cashCurrencyInDBMap, final User user) {
		for (final CashSplit split : splits) {
			split.setId(null);
			split.setUser(user);
			final CashAccount cashAccount = cashAccountInDBMap.get(split.getAccount()
					.getName());
			if (cashAccount == null) {
				throw new IllegalArgumentException();
			}
			split.setAccount(cashAccount);
			final CashCurrency cashCurrency = cashCurrencyInDBMap.get(split.getCurrency()
					.getCode());
			if (cashCurrency == null) {
				throw new IllegalArgumentException();
			}
			split.setCurrency(cashCurrency);
		}
	}

	private class CashTransactionToImportList {
		private List<CashTransactionToImport> cashTransactionsToImport;

		public List<CashTransactionToImport> getCashTransactionsToImport() {
			return cashTransactionsToImport;
		}

		public void setCashTransactionsToImport(final List<CashTransactionToImport> cashTransactionsToImport) {
			this.cashTransactionsToImport = cashTransactionsToImport;
		}
	}
}
