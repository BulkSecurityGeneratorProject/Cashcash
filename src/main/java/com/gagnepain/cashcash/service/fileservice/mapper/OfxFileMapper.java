package com.gagnepain.cashcash.service.fileservice.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashBankAccountDetails;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.enumeration.CashTransactionType;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.domain.util.JSR310DateConverters;
import net.sf.ofx4j.domain.data.banking.BankAccountDetails;
import net.sf.ofx4j.domain.data.common.Transaction;

/**
 * Mapper to transform an ofx4j object into a Cash object
 */
@Component
public class OfxFileMapper {
	public CashBankAccountDetails transform(final BankAccountDetails ofx4jObject) {
		Objects.requireNonNull(ofx4jObject);

		final CashBankAccountDetails cashObject = new CashBankAccountDetails();
		cashObject.setAccountKey(ofx4jObject.getAccountKey());
		cashObject.setAccountNumber(ofx4jObject.getAccountNumber());
		cashObject.setBankId(ofx4jObject.getBankId());
		cashObject.setBranchId(ofx4jObject.getBranchId());

		return cashObject;
	}

	public List<CashTransactionToImport> transform(final List<Transaction> ofx4jObjectList, final CashAccount cashAccount,
			final CashCurrency cashCurrencyFromBank, final Map<String, CashCurrency> cashCurrencyInDBMap) throws BusinessException {
		final List<CashTransactionToImport> transactionList = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(ofx4jObjectList)) {
			for (final Transaction ofx4jObject : ofx4jObjectList) {
				final CashTransactionToImport cashTransactionToImport = transform(ofx4jObject, cashAccount, cashCurrencyFromBank,
						cashCurrencyInDBMap);
				transactionList.add(cashTransactionToImport);
			}
		}
		return transactionList;
	}

	private CashTransactionToImport transform(final Transaction ofx4jObject, final CashAccount cashAccount,
			final CashCurrency cashCurrencyFromBank, final Map<String, CashCurrency> cashCurrencyInDBMap) throws BusinessException {
		Objects.requireNonNull(ofx4jObject);

		final CashTransactionToImport cashObject = new CashTransactionToImport();
		cashObject.setOfxId(ofx4jObject.getId());
		cashObject.setDescription(ofx4jObject.getName());
		cashObject.setTransactionDate(JSR310DateConverters.DateToZonedDateTimeConverter.INSTANCE.convert(ofx4jObject.getDatePosted()));
		cashObject.setType(CashTransactionType.convert(ofx4jObject.getTransactionType()
				.name()));

		// We create the first cashSplit
		// First one for the account corresponding to the ofxFile
		final CashSplit cashSplitFromThisAccount = new CashSplit();
		cashSplitFromThisAccount.setAmount(ofx4jObject.getBigDecimalAmount());
		cashSplitFromThisAccount.setAccount(cashAccount);

		// Define the currency
		if (ofx4jObject.getCurrency() != null && ofx4jObject.getCurrency()
				.getCode() != null) {
			final String currencyCode = ofx4jObject.getCurrency()
					.getCode();
			final CashCurrency cashCurrency = cashCurrencyInDBMap.get(currencyCode);
			if (cashCurrency == null) {
				throw new BusinessException("This currency is unknown", "currency code :" + currencyCode);
			}
			cashSplitFromThisAccount.setCurrency(cashCurrency);
		} else if (cashCurrencyFromBank != null) {
			cashSplitFromThisAccount.setCurrency(cashCurrencyFromBank);
		}

		cashObject.setSplits(new ArrayList<>());
		cashObject.getSplits()
				.add(cashSplitFromThisAccount);

		return cashObject;
	}
}
