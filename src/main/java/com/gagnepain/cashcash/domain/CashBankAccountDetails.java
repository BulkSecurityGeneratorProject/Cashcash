/*
 * Copyright 2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gagnepain.cashcash.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Base bank account details.
 */
public class CashBankAccountDetails {
	/**
	 * ETABLISSEMENT
	 */
	private String bankId;

	/**
	 * GUICHET
	 */
	private String branchId;

	private String accountNumber;

	private String accountKey;

	private String iban;

	private String bic;

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

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final CashBankAccountDetails that = (CashBankAccountDetails) o;

		if (accountKey != null ? !accountKey.equals(that.accountKey) : that.accountKey != null) {
			return false;
		}
		if (accountNumber != null ? !accountNumber.equals(that.accountNumber) : that.accountNumber != null) {
			return false;
		}
		if (bankId != null ? !bankId.equals(that.bankId) : that.bankId != null) {
			return false;
		}
		if (bic != null ? !bic.equals(that.bic) : that.bic != null) {
			return false;
		}
		if (branchId != null ? !branchId.equals(that.branchId) : that.branchId != null) {
			return false;
		}
		if (iban != null ? !iban.equals(that.iban) : that.iban != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = bankId != null ? bankId.hashCode() : 0;
		result = 31 * result + (branchId != null ? branchId.hashCode() : 0);
		result = 31 * result + (accountNumber != null ? accountNumber.hashCode() : 0);
		result = 31 * result + (accountKey != null ? accountKey.hashCode() : 0);
		result = 31 * result + (iban != null ? iban.hashCode() : 0);
		result = 31 * result + (bic != null ? bic.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("bankId", bankId)
				.append("branchId", branchId)
				.append("accountNumber", accountNumber)
				.append("accountKey", accountKey)
				.append("iban", iban)
				.append("bic", bic)
				.toString();
	}
}
