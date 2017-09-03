package com.gagnepain.cashcash.service.util;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashCsvConfig;
import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.enumeration.CashAccountType;

public class CashAccountBuilder {
	private String name;

	private CashAccountType type;

	private String code;

	private Boolean isMultiCurrency;

	private String bankId;

	private String branchId;

	private String accountNumber;

	private String accountKey;

	private String iban;

	private String bic;

	private CashCsvConfig csvConfig;

	private CashCurrency currency;

	private CashAccount parentAccount;

	private User user;

	public CashAccountBuilder() {
	}

	public CashAccountBuilder name(String name) {
		this.name = name;
		return this;
	}

	public CashAccountBuilder user(User user) {
		this.user = user;
		return this;
	}

	public CashAccountBuilder type(CashAccountType type) {
		this.type = type;
		return this;
	}

	public CashAccountBuilder code(String code) {
		this.code = code;
		return this;
	}

	public CashAccountBuilder isMultiCurrency(boolean isMultiCurrency) {
		this.isMultiCurrency = isMultiCurrency;
		return this;
	}

	public CashAccountBuilder bankId(String bankId) {
		this.bankId = bankId;
		return this;
	}

	public CashAccountBuilder branchId(String branchId) {
		this.branchId = branchId;
		return this;
	}

	public CashAccountBuilder accountKey(String accountKey) {
		this.accountKey = accountKey;
		return this;
	}

	public CashAccountBuilder iban(String iban) {
		this.iban = iban;
		return this;
	}

	public CashAccountBuilder bic(String bic) {
		this.bic = bic;
		return this;
	}

	public CashAccountBuilder csvConfig(CashCsvConfig csvConfig) {
		this.csvConfig = csvConfig;
		return this;
	}

	public CashAccountBuilder currency(CashCurrency currency) {
		this.currency = currency;
		return this;
	}

	public CashAccountBuilder parentAccount(CashAccount parentAccount) {
		this.parentAccount = parentAccount;
		return this;
	}

	public CashAccountBuilder accountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
		return this;
	}

	public CashAccount build() {
		CashAccount cashAccount = new CashAccount();
		if (null != name) {
			cashAccount.setName(name);
		} else {
			throw new IllegalArgumentException("A name must be defined");
		}
		if (null != user) {
			if (null != parentAccount) {
				throw new IllegalArgumentException("Defining the user is unnecessary when a parent account exists");
			}
			cashAccount.setUser(user);
		} else if (null == parentAccount) {
			throw new IllegalArgumentException("A user must be defined");
		}
		if (null != type) {
			if (null != parentAccount) {
				throw new IllegalArgumentException("Defining the type is unnecessary when a parent account exists");
			}
			cashAccount.setType(type);
		}
		if (null != code) {
			cashAccount.setCode(code);
		}
		if (null != isMultiCurrency) {
			cashAccount.setIsMultiCurrency(isMultiCurrency);
		}
		if (null != bankId) {
			cashAccount.setBankId(bankId);
		}
		if (null != branchId) {
			cashAccount.setBranchId(branchId);
		}
		if (null != accountNumber) {
			cashAccount.setAccountNumber(accountNumber);
		}
		if (null != accountKey) {
			cashAccount.setAccountKey(accountKey);
		}
		if (null != iban) {
			cashAccount.setIban(iban);
		}
		if (null != bic) {
			cashAccount.setBic(bic);
		}
		if (null != csvConfig) {
			cashAccount.setCsvConfig(csvConfig);
		}
		if (null != currency) {
			cashAccount.setCurrency(currency);
		} else if (null != parentAccount) {
			cashAccount.setCurrency(parentAccount.getCurrency());
		} else {
			throw new IllegalArgumentException("Currency has to be defined or a parent account must exit.");
		}
		if (null == parentAccount) {
			cashAccount.setLevel(1);
		} else {
			cashAccount.setParentAccount(parentAccount);
			cashAccount.setLevel(parentAccount.getLevel() + 1);
			cashAccount.setType(parentAccount.getType());
			cashAccount.setUser(parentAccount.getUser());
		}
		return cashAccount;
	}
}
