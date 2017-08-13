package com.gagnepain.cashcash.web.rest.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * An external cashRate.
 */
public class ExternalCashRateDTO {
	private static final long serialVersionUID = 1L;

	private String base;

	private LocalDate date;

	private Map<String, BigDecimal> rates;

	public String getBase() {
		return base;
	}

	public void setBase(final String base) {
		this.base = base;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(final LocalDate date) {
		this.date = date;
	}

	public Map<String, BigDecimal> getRates() {
		return rates;
	}

	public void setRates(final Map<String, BigDecimal> rates) {
		this.rates = rates;
	}
}
