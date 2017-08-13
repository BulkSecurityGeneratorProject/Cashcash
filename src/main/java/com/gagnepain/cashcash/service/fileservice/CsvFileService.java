package com.gagnepain.cashcash.service.fileservice;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.prefs.CsvPreference;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCsvConfig;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.service.CashAccountService;
import com.gagnepain.cashcash.service.CashCurrencyService;
import com.gagnepain.cashcash.service.fileservice.mapper.CashTransactionToImport;
import com.gagnepain.cashcash.service.fileservice.mapper.CsvFileMapper;

/**
 * Service class for ofx file
 */
@Service
@Transactional
public class CsvFileService {
	@Inject
	CashAccountService cashAccountService;

	@Inject
	CashCurrencyService cashCurrencyService;

	@Inject
	private CsvFileMapper csvFileMapper;

	@Inject
	private CommonFileService commonFileService;

	private final Logger log = LoggerFactory.getLogger(CsvFileService.class);

	public List<CashTransactionToImport> extractTransaction(final InputStream inputStream, final Long accountId)
			throws IOException, BusinessException, TikaException {

		CashAccount cashAccount = null;
		CashCsvConfig csvConfig = null;
		if (accountId == null) {
			throw new BusinessException("account has to be provided for csv import", null);
		} else {
			cashAccount = cashAccountService.findOne(accountId);
			if (cashAccount == null) {
				throw new BusinessException("account not found", "accountId :" + accountId);
			}
			csvConfig = cashAccount.getCsvConfig();
			if (csvConfig == null) {
				throw new BusinessException("no csv config attached to this account", "accountName :" + cashAccount.getName());
			}
		}

		final BufferedInputStream stream = new BufferedInputStream(inputStream);
		stream.mark(stream.available());

		final Charset charset;
		if (StringUtils.isNotBlank(csvConfig.getCharset())) {
			charset = Charset.forName(csvConfig.getCharset());
		} else {
			final AutoDetectReader autoDetectReader = new AutoDetectReader(stream);
			charset = autoDetectReader.getCharset();
			stream.reset();
		}

		final List<CashCurrency> cashCurrenciesInDB = cashCurrencyService.findAll();
		final Map<String, CashCurrency> cashCurrencyInDBMap = cashCurrenciesInDB.stream()
				.collect(Collectors.toMap(CashCurrency::getCode, Function.identity()));

		final List<CashTransactionToImport> cashTransactionToImportList = new ArrayList<>();

		final char quoteChar = csvConfig.getQuoteChar();
		final char delimiterChar = csvConfig.getDelimiterChar();
		final String endOfLineSymbols = csvConfig.getEndOfLineSymbols();
		final CsvPreference csvPreference = new CsvPreference.Builder(quoteChar, delimiterChar, endOfLineSymbols).build();

		try (ICsvListReader listReader = new CsvListReader(new BufferedReader(new InputStreamReader(stream, charset)), csvPreference)) {
			listReader.getHeader(csvConfig.isHasHeader());
			while (listReader.read() != null) {
				final CashTransactionToImport cashTransactionToImport = csvFileMapper.mapToTransactions(listReader, cashAccount, csvConfig,
						cashCurrencyInDBMap);
				cashTransactionToImportList.add(cashTransactionToImport);
			}
		}

		// enrich transaction information
		commonFileService.enrichTransaction(cashTransactionToImportList);

		return cashTransactionToImportList;
	}
}
