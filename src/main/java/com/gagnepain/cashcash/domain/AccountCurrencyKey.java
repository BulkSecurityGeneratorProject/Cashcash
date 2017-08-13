package com.gagnepain.cashcash.domain;

/**
 * Key use to simplify the key of map of CashAccount
 */
public class AccountCurrencyKey {
	final long accountId;

	final long currencyId;

	public AccountCurrencyKey(final long accountId, final long currencyId) {
		this.accountId = accountId;
		this.currencyId = currencyId;
	}

	public long getAccountId() {
		return accountId;
	}

	public long getCurrencyId() {
		return currencyId;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final AccountCurrencyKey that = (AccountCurrencyKey) o;

		if (accountId != that.accountId) {
			return false;
		}
		return currencyId == that.currencyId;
	}

	@Override
	public int hashCode() {
		int result = (int) (accountId ^ (accountId >>> 32));
		result = 31 * result + (int) (currencyId ^ (currencyId >>> 32));
		return result;
	}
}
