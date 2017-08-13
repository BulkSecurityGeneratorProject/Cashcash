package com.gagnepain.cashcash.domain;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.gagnepain.cashcash.domain.enumeration.CashTransactionType;

/**
 * A CashTransaction.
 */
@Entity
@Table(name = "cash_transaction")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class,
		property = "@cashTransactionId")
public class CashTransaction extends CashOwnedResource {
	private static final long serialVersionUID = 1L;

	public CashTransaction() {
		super();
	}

	public CashTransaction(final CashTransaction cashTransaction) {
		super(cashTransaction);
		this.description = cashTransaction.description;
		this.ofxId = cashTransaction.ofxId;
		this.transactionDate = cashTransaction.transactionDate;
		this.type = cashTransaction.type;
		this.splits = cashTransaction.getSplits();
	}

	@NotNull
	@Size(min = 2,
			max = 100)
	@Column(name = "description",
			length = 100,
			nullable = false)
	private String description;

	@Size(min = 2,
			max = 50)
	@Column(name = "ofx_id",
			length = 50)
	private String ofxId;

	@NotNull
	@Column(name = "transaction_date",
			nullable = false)
	private ZonedDateTime transactionDate;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "type",
			nullable = false)
	private CashTransactionType type;

	@OneToMany(mappedBy = "transaction",
			fetch = FetchType.EAGER,
			cascade = CascadeType.ALL)
	@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
	private List<CashSplit> splits;

	@Override
	public List<CashOwnedResource> getOwnedResources() {
		final List<CashOwnedResource> independantResources = super.getOwnedResources();
		for (final CashSplit split : splits) {
			independantResources.addAll(split.getOwnedResources());
		}
		return independantResources;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(final String description) {
		this.description = description;
	}

	public String getOfxId() {
		return ofxId;
	}

	public void setOfxId(final String ofxId) {
		this.ofxId = ofxId;
	}

	public ZonedDateTime getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(final ZonedDateTime transactionDate) {
		this.transactionDate = transactionDate;
	}

	public CashTransactionType getType() {
		return type;
	}

	public void setType(final CashTransactionType type) {
		this.type = type;
	}

	public List<CashSplit> getSplits() {
		return splits;
	}

	public void setSplits(final List<CashSplit> cashSplits) {
		this.splits = cashSplits;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CashTransaction cashTransaction = (CashTransaction) o;
		if (cashTransaction.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), cashTransaction.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CashTransaction{" + "id=" + getId() + ", description='" + description + "'" + ", ofxId='" + ofxId + "'" +
				", transactionDate='" + transactionDate + "'" + ", type='" + type + "'" + '}';
	}
}
