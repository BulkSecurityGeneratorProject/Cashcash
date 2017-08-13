package com.gagnepain.cashcash.domain;

import java.math.BigDecimal;

/**
 * A Cash Split Cumulative.
 */
public class CashSplitCumulative extends CashSplit {
	private BigDecimal cumulativeAmount;

	private CashCurrency cumulativeCurrency;

	private Long cumulativeCurrencyId;

	public CashSplitCumulative(CashSplit cashSplit, BigDecimal cumulativeAmount, CashCurrency cumulativeCurrency) {
		super(cashSplit);
		this.cumulativeAmount = cumulativeAmount;
		this.cumulativeCurrency = cumulativeCurrency;
		this.cumulativeCurrencyId = cumulativeCurrency.getId();
	}

	public BigDecimal getCumulativeAmount() {
		return cumulativeAmount;
	}

	public void setCumulativeAmount(final BigDecimal cumulativeAmount) {
		this.cumulativeAmount = cumulativeAmount;
	}

	public CashCurrency getCumulativeCurrency() {
		return cumulativeCurrency;
	}

	public void setCumulativeCurrency(final CashCurrency cumulativeCurrency) {
		this.cumulativeCurrency = cumulativeCurrency;
	}

	public Long getCumulativeCurrencyId() {
		return cumulativeCurrencyId;
	}

	public void setCumulativeCurrencyId(final Long cumulativeCurrencyId) {
		this.cumulativeCurrencyId = cumulativeCurrencyId;
	}
}
