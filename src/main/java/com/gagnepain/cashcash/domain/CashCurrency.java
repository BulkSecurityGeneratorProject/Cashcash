package com.gagnepain.cashcash.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CashCurrency.
 */
@Entity
@Table(name = "cash_currency")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashCurrency extends CashResource {
	private static final long serialVersionUID = 1L;

	/**
	 * The currency code, not null.
	 */
	@NotNull
	@Size(min = 3,
			max = 3)
	@Column(name = "code",
			length = 3,
			nullable = false)
	private String code;

	/**
	 * The numeric currency code.
	 */
	@Column(name = "numeric_code")
	private Integer numericCode;

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Integer getNumericCode() {
		return numericCode;
	}

	public void setNumericCode(final Integer numericCode) {
		this.numericCode = numericCode;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CashCurrency cashCurrency = (CashCurrency) o;
		if (cashCurrency.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), cashCurrency.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CashCurrency{" + "id=" + getId() + ", code='" + code + "'" + ", numericCode='" + numericCode + "'" + '}';
	}
}
