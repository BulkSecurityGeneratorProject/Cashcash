package com.gagnepain.cashcash.domain;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gagnepain.cashcash.domain.enumeration.CashTransactionType;

/**
 * A CashSplit.
 */
@Entity
@Table(name = "cash_split")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
		property = "@cashSplitId")
public class CashSplit extends CashOwnedResource {
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

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL)
	@NotNull
	private CashTransaction transaction;

	@Column(name = "transaction_id",
			updatable = false,
			insertable = false)
	private Long transactionId;

	@NotNull
	@Size(min = 2,
			max = 100)
	@Column(name = "transaction_description",
			length = 100,
			nullable = false)
	private String transactionDescription;

	@NotNull
	@Column(name = "transaction_date",
			nullable = false)
	private ZonedDateTime transactionDate;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type",
			nullable = false)
	private CashTransactionType transactionType;

	public CashSplit() {
	}

	public CashSplit(final CashSplit cashSplit) {
		super(cashSplit);
		this.amount = cashSplit.amount;
		this.account = cashSplit.account;
		this.accountId = cashSplit.accountId;
		this.currency = cashSplit.currency;
		this.currencyId = cashSplit.currencyId;
		this.transaction = cashSplit.transaction;
		this.transactionId = cashSplit.transactionId;
		this.transactionDescription = cashSplit.transactionDescription;
		this.transactionDate = cashSplit.transactionDate;
		this.transactionType = cashSplit.transactionType;
	}

	@Override
	public List<CashOwnedResource> getOwnedResources() {
		final List<CashOwnedResource> independantResources = super.getOwnedResources();

		if (transaction != null) {
			independantResources.add(transaction);
		}
		if (account != null) {
			independantResources.add(account);
		}
		return independantResources;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(final BigDecimal amount) {
		this.amount = amount;
	}

	public CashAccount getAccount() {
		return account;
	}

	public void setAccount(final CashAccount cashAccount) {
		this.account = cashAccount;
	}

	public CashCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(final CashCurrency cashCurrency) {
		this.currency = cashCurrency;
	}

	public CashTransaction getTransaction() {
		return transaction;
	}

	public void setTransaction(final CashTransaction cashTransaction) {
		this.transaction = cashTransaction;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(final Long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionDescription() {
		return transactionDescription;
	}

	public void setTransactionDescription(final String transactionDescription) {
		this.transactionDescription = transactionDescription;
	}

	public ZonedDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(final ZonedDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public CashTransactionType getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(final CashTransactionType transactionType) {
		this.transactionType = transactionType;
	}

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(final Long accountId) {
		this.accountId = accountId;
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
		final CashSplit cashSplit = (CashSplit) o;
		if (cashSplit.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), cashSplit.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CashSplit{" + "id=" + getId() + ", amount='" + amount + "'" + '}';
	}
}
