package com.gagnepain.cashcash.domain;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import com.gagnepain.cashcash.domain.util.JSR310LocalDateDeserializer;

/**
 * Hold the parameters to use for filtering the request
 */
public class FilterParams {
	private List<Long> accountIdList;

	private String currencyCode;

	private ZonedDateTime startDate;

	private ZonedDateTime endDate;

	public List<Long> getAccountIdList() {
		return accountIdList;
	}

	public void setAccountIdList(final List<Long> accountIdList) {
		this.accountIdList = accountIdList;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(final String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public ZonedDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(final String startDateString) {
		final LocalDate localDate = LocalDate.parse(startDateString, JSR310LocalDateDeserializer.ISO_DATE_OPTIONAL_TIME);
		this.startDate = localDate.atStartOfDay(ZoneId.systemDefault());
	}

	public ZonedDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(final String endDateString) {
		final LocalDate localDate = LocalDate.parse(endDateString, JSR310LocalDateDeserializer.ISO_DATE_OPTIONAL_TIME);
		this.endDate = localDate.atStartOfDay(ZoneId.systemDefault());
	}
}
