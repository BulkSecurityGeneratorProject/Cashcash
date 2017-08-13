package com.gagnepain.cashcash.service.fileservice.mapper;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.gagnepain.cashcash.domain.CashTransaction;

/**
 * A class to wrap a CashTransaction Object in order to indicate info about the import
 */
public class CashTransactionToImport extends CashTransaction {
	/**
	 * Indiquate if the ofxId already Exist into the database
	 */
	private boolean ofxIdAlreadyExist;

	public CashTransactionToImport() {
	}

	public CashTransactionToImport(final CashTransaction cashTransaction) {
		super(cashTransaction);
	}

	public boolean isOfxIdAlreadyExist() {
		return ofxIdAlreadyExist;
	}

	public void setOfxIdAlreadyExist(final boolean ofxIdAlreadyExist) {
		this.ofxIdAlreadyExist = ofxIdAlreadyExist;
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

		final CashTransactionToImport that = (CashTransactionToImport) o;

		if (ofxIdAlreadyExist != that.ofxIdAlreadyExist) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (ofxIdAlreadyExist ? 1 : 0);
		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("ofxIdAlreadyExist", ofxIdAlreadyExist)
				.toString();
	}
}
