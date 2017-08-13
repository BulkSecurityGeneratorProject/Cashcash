package com.gagnepain.cashcash.service.util;

import org.apache.commons.lang3.EnumUtils;

/**
 * Liste of the supported currencies based on exchange rate http://api.fixer.io can give
 */
public enum SupportedCurrencies {
	AUD,
	BGN,
	BRL,
	CAD,
	CHF,
	CNY,
	CZK,
	DKK,
	EUR,
	GBP,
	HKD,
	HRK,
	HUF,
	IDR,
	ILS,
	INR,
	JPY,
	KRW,
	MXN,
	MYR,
	NOK,
	NZD,
	PHP,
	PLN,
	RON,
	RUB,
	SEK,
	SGD,
	THB,
	TRY,
	USD,
	ZAR;

	public static boolean isSupportedCurrency(String code){
		return EnumUtils.isValidEnum(SupportedCurrencies.class, code);
	}
}
