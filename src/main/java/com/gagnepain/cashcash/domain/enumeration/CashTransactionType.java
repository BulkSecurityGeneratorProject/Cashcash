package com.gagnepain.cashcash.domain.enumeration;

import org.apache.commons.lang3.StringUtils;

/**
 * The CashTransactionType enumeration.
 */
public enum CashTransactionType {
	CREDIT,
	DEBIT,
	INT,
	DIV,
	FEE,
	SRVCHG,
	DEP,
	ATM,
	POS,
	XFER,
	CHECK,
	PAYMENT,
	CASH,
	DIRECTDEP,
	DIRECTDEBIT,
	REPEATPMT,
	OTHER;

	public static CashTransactionType convert(String name) {
		if (name == null) {
			return OTHER;
		}
		for (CashTransactionType cashTransactionType : values()) {
			if (StringUtils.equalsIgnoreCase(cashTransactionType.name(), name)) {
				return cashTransactionType;
			}
		}

		return OTHER;
	}
}
