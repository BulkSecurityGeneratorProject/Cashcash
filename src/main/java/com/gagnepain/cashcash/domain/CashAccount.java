package com.gagnepain.cashcash.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.gagnepain.cashcash.domain.enumeration.CashAccountType;

/**
 * A CashAccount.
 */
@Entity
@Table(name = "cash_account")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashAccount extends CashOwnedResource {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 2,
			max = 80)
	@Column(name = "name",
			length = 80,
			nullable = false)
	private String name;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(name = "type",
			nullable = false)
	private CashAccountType type;

	@NotNull
	@Min(value = 1)
	@Column(name = "level",
			nullable = false)
	private int level;

	@Size(min = 1,
			max = 10)
	@Column(name = "code",
			length = 10)
	private String code;

	@NotNull
	@Column(name = "is_multi_currency",
			nullable = false)
	private boolean isMultiCurrency;

	@Column(name = "bank_id")
	private String bankId;

	@Column(name = "branch_id")
	private String branchId;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "account_key")
	private String accountKey;

	@Column(name = "iban")
	private String iban;

	@Column(name = "bic")
	private String bic;

	@ManyToOne(fetch = FetchType.LAZY)
	private CashCsvConfig csvConfig;

	@Column(name = "csv_config_id",
			updatable = false,
			insertable = false)
	private Long csvConfigId;

	@ManyToOne
	@NotNull
	private CashCurrency currency;

	@Column(name = "currency_id",
			updatable = false,
			insertable = false)
	private Long currencyId;

	@ManyToOne
	@JsonBackReference
	private CashAccount parentAccount;

	@Column(name = "parent_account_id",
			updatable = false,
			insertable = false)
	private Long parentAccountId;

	@OneToMany(cascade = CascadeType.PERSIST,
			mappedBy = "parentAccount",
			fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<CashAccount> childAccountList;

	@Override
	public List<CashOwnedResource> getOwnedResources() {
		final List<CashOwnedResource> independantResources = super.getOwnedResources();
		if (parentAccount != null) {
			independantResources.add(parentAccount);
		}
		if (csvConfig != null) {
			independantResources.add(csvConfig);
		}
		return independantResources;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public CashAccountType getType() {
		return type;
	}

	public void setType(final CashAccountType type) {
		this.type = type;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		this.level = level;
	}

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public boolean isMultiCurrency() {
		return isMultiCurrency;
	}

	public void setIsMultiCurrency(final boolean isMultiCurrency) {
		this.isMultiCurrency = isMultiCurrency;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(final String bankId) {
		this.bankId = bankId;
	}

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(final String branchId) {
		this.branchId = branchId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(final String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountKey() {
		return accountKey;
	}

	public void setAccountKey(final String accountKey) {
		this.accountKey = accountKey;
	}

	public String getIban() {
		return iban;
	}

	public void setIban(final String iban) {
		this.iban = iban;
	}

	public String getBic() {
		return bic;
	}

	public void setBic(final String bic) {
		this.bic = bic;
	}

	public CashCsvConfig getCsvConfig() {
		return csvConfig;
	}

	public void setCsvConfig(final CashCsvConfig csvConfig) {
		this.csvConfig = csvConfig;
	}

	public CashCurrency getCurrency() {
		return currency;
	}

	public void setCurrency(final CashCurrency currency) {
		this.currency = currency;
	}

	public CashAccount getParentAccount() {
		return parentAccount;
	}

	public void setParentAccount(final CashAccount parentAccount) {
		this.parentAccount = parentAccount;
		this.parentAccount.getChildAccountList()
				.add(this);
	}

	public Long getParentAccountId() {
		return parentAccountId;
	}

	public void setParentAccountId(final Long parentAccountId) {
		this.parentAccountId = parentAccountId;
	}

	public Long getCsvConfigId() {
		return csvConfigId;
	}

	public void setCsvConfigId(final Long csvConfigId) {
		this.csvConfigId = csvConfigId;
	}

	public Long getCurrencyId() {
		return currencyId;
	}

	public void setCurrencyId(final Long currencyId) {
		this.currencyId = currencyId;
	}

	public Set<CashAccount> getChildAccountList() {
		if (childAccountList == null) {
			childAccountList = new HashSet<>();
		}
		return childAccountList;
	}

	public void setChildAccountList(final Set<CashAccount> childAccountList) {
		this.childAccountList = childAccountList;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CashAccount cashAccount = (CashAccount) o;
		if (cashAccount.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), cashAccount.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CashAccount{" + "name='" + name + '\'' + ", type=" + type + ", level=" + level + ", code='" + code + '\'' +
				", isMultiCurrency=" + isMultiCurrency + ", bankId='" + bankId + '\'' + ", branchId='" + branchId + '\'' +
				", accountNumber='" + accountNumber + '\'' + ", accountKey='" + accountKey + '\'' + ", iban='" + iban + '\'' + ", bic='" +
				bic + '\'' + ", csvConfig=" + csvConfig + ", currency=" + currency + ", parentAccount=" + parentAccount + '}';
	}
}
