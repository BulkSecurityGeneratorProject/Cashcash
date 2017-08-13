package com.gagnepain.cashcash.domain;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.gagnepain.cashcash.repository.CashRateMapConverter;

/**
 * A CashRate.
 */
@Entity
@Table(name = "cash_rate")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashRate extends CashResource {
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
	 * The list of rate in json.
	 */
	@Column(name = "rates")
	@Convert(converter = CashRateMapConverter.class)
	private Map<String, BigDecimal> rates;

	public String getCode() {
		return code;
	}

	public void setCode(final String code) {
		this.code = code;
	}

	public Map<String, BigDecimal> getRates() {
		return rates;
	}

	public void setRates(final Map<String, BigDecimal> rates) {
		this.rates = rates;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CashRate cashCurrency = (CashRate) o;
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
		return "CashCurrency{" + "id=" + getId() + ", code='" + code + "'" + '}';
	}
}
