package com.gagnepain.cashcash.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Cash Split Sum.
 */
@Entity
@Table(name = "cash_split_sum")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashSplitSum extends CashOwnedResource {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Column(name = "amount",
			precision = 10,
			scale = 2,
			nullable = false)
	private BigDecimal amount;

	@ManyToOne
	@NotNull
	private CashAccount account;

	@Column(name = "account_id",
			updatable = false,
			insertable = false)
	private Long accountId;

	@ManyToOne
	@NotNull
	private CashCurrency currency;

	@Column(name = "currency_id",
			updatable = false,
			insertable = false)
	private Long currencyId;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public CashAccount getAccount() {
		return account;
	}

	public void setAccount(final CashAccount account) {
		this.account = account;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(final Long accountId) {
		this.accountId = accountId;
	}

	public CashCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(final CashCurrency currency) {
		this.currency = currency;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(final Long currencyId) {
		this.currencyId = currencyId;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		final CashSplitSum that = (CashSplitSum) o;

		if (amount != null ? !amount.equals(that.amount) : that.amount != null) {
			return false;
		}
		if (account != null ? !account.equals(that.account) : that.account != null) {
			return false;
		}
		return currency != null ? currency.equals(that.currency) : that.currency == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (amount != null ? amount.hashCode() : 0);
		result = 31 * result + (account != null ? account.hashCode() : 0);
		result = 31 * result + (currency != null ? currency.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "CashSplitSum{" + "amount=" + amount + ", account=" + account + ", currency=" + currency + '}';
	}
}
