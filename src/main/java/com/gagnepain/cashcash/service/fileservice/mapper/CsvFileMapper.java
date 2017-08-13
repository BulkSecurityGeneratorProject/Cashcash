package com.gagnepain.cashcash.service.fileservice.mapper;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import javax.inject.Inject;

import org.springframework.stereotype.Component;
import org.supercsv.io.ICsvListReader;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCsvConfig;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.enumeration.CashTransactionType;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.service.CashAccountService;

@Component
public class CsvFileMapper {
	@Inject
	private CashAccountService cashAccountService;

	public CashTransactionToImport mapToTransactions(final ICsvListReader listReader, final CashAccount cashAccount,
			final CashCsvConfig csvConfig, final Map<String, CashCurrency> cashCurrencyInDBMap) throws BusinessException {
		final Optional<Integer> uniqIdColumnIndex = Optional.ofNullable(csvConfig.getUniqIdColumnIndex());
		final Optional<Integer> descriptionColumnIndex = Optional.ofNullable(csvConfig.getDescriptionColumnIndex());
		final Optional<Integer> detailsColumnIndex = Optional.ofNullable(csvConfig.getDetailDescriptionColumnIndex());
		final Optional<Integer> accountCodeNumberIndex = Optional.ofNullable(csvConfig.getAccountCodeNumberColumnIndex());
		final int transactionDateColumnIndex = csvConfig.getTransactionDateColumnIndex();
		final String transactionDateFormat = csvConfig.getTransactionDateFormat();
		final Optional<Integer> transactionTypeColumnIndex = Optional.ofNullable(csvConfig.getTransactionTypeColumnIndex());
		final int amountColumnIndex = csvConfig.getAmountColumnIndex();
		final char decimalDelimiter = csvConfig.getDecimalDelimiter();
		final Optional<Integer> currencyColumnIndex = Optional.ofNullable(csvConfig.getCurrencyColumnIndex());

		final CashAccount transactionCashAccount;
		if (accountCodeNumberIndex.isPresent() && cashAccount == null) {
			final String cashAccountNumber = listReader.get(accountCodeNumberIndex.get());
			transactionCashAccount = cashAccountService.findOneByAccountNumber(cashAccountNumber);
			if (transactionCashAccount == null) {
				throw new BusinessException("account not found", "accountNumber :" + cashAccountNumber);
			}
		} else {
			transactionCashAccount = cashAccount;
		}

		final CashTransactionToImport cashObject = new CashTransactionToImport();
		if (uniqIdColumnIndex.isPresent()) {
			cashObject.setOfxId(listReader.get(uniqIdColumnIndex.get()));
		}
		if (descriptionColumnIndex.isPresent()) {
			cashObject.setDescription(listReader.get(descriptionColumnIndex.get()));
		}
		if (detailsColumnIndex.isPresent()) {
			cashObject.setDescription(
					listReader.get(descriptionColumnIndex.get()) + ", details: " + listReader.get(detailsColumnIndex.get()));
		}

		final DateTimeFormatter formatter = new DateTimeFormatterBuilder().appendPattern(transactionDateFormat)
				.parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
				.parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
				.parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
				.parseDefaulting(ChronoField.OFFSET_SECONDS, 0)
				.toFormatter();
		final ZonedDateTime zonedDateTime = ZonedDateTime.parse(listReader.get(transactionDateColumnIndex), formatter);
		cashObject.setTransactionDate(zonedDateTime);

		if (transactionTypeColumnIndex.isPresent()) {
			cashObject.setType(CashTransactionType.convert(listReader.get(transactionTypeColumnIndex.get())));
		} else {
			cashObject.setType(CashTransactionType.OTHER);
		}

		// We create the first cashSplit
		// First one for the account corresponding to the ofxFile
		final CashSplit cashSplitFromThisAccount = new CashSplit();
		final String amountAsString = listReader.get(amountColumnIndex)
				.replace(decimalDelimiter, '.')
				.trim();
		final BigDecimal amount = new BigDecimal(amountAsString);
		cashSplitFromThisAccount.setAmount(amount);
		cashSplitFromThisAccount.setAccount(transactionCashAccount);

		if (currencyColumnIndex.isPresent()) {
			final String currencyCode = listReader.get(currencyColumnIndex.get());
			final CashCurrency cashCurrency = cashCurrencyInDBMap.get(currencyCode);
			if (cashCurrency == null) {
				throw new BusinessException("This currency is unknown", "currency code :" + currencyCode);
			}
			cashSplitFromThisAccount.setCurrency(cashCurrency);
		} else {
			cashSplitFromThisAccount.setCurrency(transactionCashAccount.getCurrency());
		}

		cashObject.setSplits(new ArrayList<>());
		cashObject.getSplits()
				.add(cashSplitFromThisAccount);

		return cashObject;
	}
}
