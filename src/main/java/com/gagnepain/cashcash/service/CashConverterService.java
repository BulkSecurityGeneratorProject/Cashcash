package com.gagnepain.cashcash.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gagnepain.cashcash.domain.CashRate;
import com.gagnepain.cashcash.domain.exception.BusinessException;

/**
 * Service Implementation for converting CashAmount.
 */
@Service
public class CashConverterService {
	@Inject
	private CashRateService cashRateService;

	/**
	 * Convert a cashAmount into another currency
	 */
	public BigDecimal convert(final BigDecimal cashAmount, final String currentCurrencyCode, final String expectedCurrencyCode)
			throws BusinessException {
		if (StringUtils.equals(currentCurrencyCode, expectedCurrencyCode)) {
			return cashAmount;
		}

		CashRate cashRate = cashRateService.findOne(expectedCurrencyCode);
		BigDecimal actualToExpectedRate = cashRate.getRates()
				.get(currentCurrencyCode);

		return cashAmount.divide(actualToExpectedRate, RoundingMode.HALF_UP);
	}
}
