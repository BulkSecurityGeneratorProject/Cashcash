package com.gagnepain.cashcash.service.fileservice;

import java.util.List;
import java.util.Optional;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.CashTransaction;
import com.gagnepain.cashcash.service.CashTransactionService;
import com.gagnepain.cashcash.service.fileservice.mapper.CashTransactionToImport;

/**
 * Class which gather common useful methods
 */
@Service
@Transactional
public class CommonFileService {
	@Inject
	private CashTransactionService cashTransactionService;

	private final Logger log = LoggerFactory.getLogger(CommonFileService.class);

	public void enrichTransaction(final List<CashTransactionToImport> cashTransactionToImportList) {

		// We retrieve the last 200 transactions to try to find similarities
		List<CashTransaction> freshCashTransactionInDB = cashTransactionService.findFreshCashTransaction();

		for (final CashTransactionToImport cashTransactionToImport : cashTransactionToImportList) {
			detectAlreadyImportedTransaction(cashTransactionToImport, freshCashTransactionInDB);
			fillInfoBasedOnSimilarTransaction(cashTransactionToImport, freshCashTransactionInDB);
		}
	}

	private void fillInfoBasedOnSimilarTransaction(final CashTransactionToImport cashTransactionToImport, List<CashTransaction>
			freshCashTransactionInDB) {
		final String description = cashTransactionToImport.getDescription();
		final CashSplit firstSplitOfImportTransaction = cashTransactionToImport.getSplits()
				.get(0);

		final Optional<CashAccount> otherAccountGuessBasedOnDB = guessOtherAccountBasedOnDB(firstSplitOfImportTransaction.getAccount(),
				description, freshCashTransactionInDB);

		// We consider the transaction to import as a basic transaction
		final CashSplit otherPartOfTheSplit = new CashSplit();
		otherPartOfTheSplit.setCurrency(firstSplitOfImportTransaction.getCurrency());
		otherPartOfTheSplit.setAmount(firstSplitOfImportTransaction.getAmount()
				.negate());
		otherAccountGuessBasedOnDB.ifPresent(otherPartOfTheSplit::setAccount);
		cashTransactionToImport.getSplits()
				.add(otherPartOfTheSplit);
	}

	private Optional<CashAccount> guessOtherAccountBasedOnDB(final CashAccount cashAccount, final String description,
			List<CashTransaction> freshCashTransactionInDB) {

		Optional<CashTransaction> similarTransaction = freshCashTransactionInDB.stream()
				.filter(cashTransaction -> StringUtils.equals(cashTransaction.getDescription(), description))
				.findFirst();
		if (similarTransaction.isPresent() && similarTransaction.get()
				.getSplits()
				.size() == 2) {
			// It's a basic transaction
			// We can find the other account
			final CashSplit otherSplitFromSimilarTransaction = similarTransaction.get()
					.getSplits()
					.stream()
					.filter(split -> !split.getAccount()
							.equals(cashAccount))
					.findAny()
					.get();

			return Optional.of(otherSplitFromSimilarTransaction.getAccount());
		} else {
			return Optional.empty();
		}
	}

	private void detectAlreadyImportedTransaction(final CashTransactionToImport cashTransactionToImport,
			List<CashTransaction> freshCashTransactionInDB) {
		final String ofxId = cashTransactionToImport.getOfxId();

		boolean ofxIdExistInDB = freshCashTransactionInDB.stream()
				.anyMatch(cashTransaction -> StringUtils.equals(cashTransaction.getOfxId(), ofxId));
		cashTransactionToImport.setOfxIdAlreadyExist(ofxIdExistInDB);
	}
}
