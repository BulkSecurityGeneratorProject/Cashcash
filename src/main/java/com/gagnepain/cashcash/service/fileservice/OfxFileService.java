package com.gagnepain.cashcash.service.fileservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashBankAccountDetails;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.service.CashAccountService;
import com.gagnepain.cashcash.service.CashCurrencyService;
import com.gagnepain.cashcash.service.fileservice.mapper.CashTransactionToImport;
import com.gagnepain.cashcash.service.fileservice.mapper.OfxFileMapper;
import net.sf.ofx4j.domain.data.MessageSetType;
import net.sf.ofx4j.domain.data.ResponseEnvelope;
import net.sf.ofx4j.domain.data.banking.BankStatementResponse;
import net.sf.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import net.sf.ofx4j.domain.data.banking.BankingResponseMessageSet;
import net.sf.ofx4j.domain.data.common.TransactionList;
import net.sf.ofx4j.io.AggregateUnmarshaller;
import net.sf.ofx4j.io.OFXParseException;

/**
 * Service class for ofx file
 */
@Service
public class OfxFileService {
	@Inject
	private OfxFileMapper ofxFileMapper;

	@Inject
	private CashAccountService cashAccountService;

	@Inject
	private CashCurrencyService cashCurrencyService;

	@Inject
	private CommonFileService commonFileService;

	private final Logger log = LoggerFactory.getLogger(OfxFileService.class);

	public List<CashTransactionToImport> extractTransaction(final InputStream inputStream, final Long accountId)
			throws IOException, OFXParseException, BusinessException {

		CashAccount cashAccount = null;
		if (accountId != null) {
			cashAccount = cashAccountService.findOne(accountId);
			if (cashAccount == null) {
				throw new BusinessException("account not found", "accountId :" + accountId);
			}
		}

		//Unmarshal
		final BankingResponseMessageSet messageSet = getBankingResponseMessageSet(inputStream);

		//Convert
		final List<CashTransactionToImport> cashTransactionToImportList = convertToCashCashObjects(messageSet, cashAccount);

		// enrich transaction information
		commonFileService.enrichTransaction(cashTransactionToImportList);

		return cashTransactionToImportList;
	}

	private List<CashTransactionToImport> convertToCashCashObjects(final BankingResponseMessageSet messageSet,
			final CashAccount cashAccount) throws BusinessException {
		final List<CashTransactionToImport> cashTransactionList = new ArrayList<>();

		final List<CashCurrency> cashCurrenciesInDB = cashCurrencyService.findAll();
		final Map<String, CashCurrency> cashCurrencyInDBMap = cashCurrenciesInDB.stream()
				.collect(Collectors.toMap(CashCurrency::getCode, Function.identity()));

		for (final BankStatementResponseTransaction responseTransaction : messageSet.getStatementResponses()) {
			final BankStatementResponse bankStatementResponse = responseTransaction.getMessage();

			// Get the bank
			final CashCurrency cashCurrencyFromBank = cashCurrencyService.findOne(bankStatementResponse.getCurrencyCode());

			final CashAccount cashAccountFromBank;
			if (cashAccount == null) {
				//Search for account
				final CashBankAccountDetails cashBankAccountDetails = ofxFileMapper.transform(bankStatementResponse.getAccount());
				cashAccountFromBank = cashAccountService.findOneByAccountNumber(cashBankAccountDetails.getAccountNumber());
				if (cashAccount == null) {
					throw new BusinessException("account not found", "accountNumber :" + cashBankAccountDetails.getAccountNumber());
				}
			} else {
				cashAccountFromBank = cashAccount;
			}

			final TransactionList transactionList = bankStatementResponse.getTransactionList();
			if (transactionList != null && transactionList.getTransactions() != null) {
				final List<CashTransactionToImport> transformedTransactions = ofxFileMapper.transform(transactionList.getTransactions(),
						cashAccountFromBank, cashCurrencyFromBank, cashCurrencyInDBMap);
				cashTransactionList.addAll(transformedTransactions);
			}
		}

		return cashTransactionList;
	}

	private BankingResponseMessageSet getBankingResponseMessageSet(final InputStream inputStream) throws IOException, OFXParseException {
		final AggregateUnmarshaller<ResponseEnvelope> unmarshaller = new AggregateUnmarshaller<>(ResponseEnvelope.class);

		final ResponseEnvelope response = unmarshaller.unmarshal(inputStream);

		final BankingResponseMessageSet messageSet = (BankingResponseMessageSet) response.getMessageSet(MessageSetType.banking);
		Objects.requireNonNull(messageSet);
		return messageSet;
	}
}
