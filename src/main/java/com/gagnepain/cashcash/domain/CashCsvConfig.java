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
 * A csv config
 */
@Entity
@Table(name = "cash_csv_config")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CashCsvConfig extends CashOwnedResource {
	private static final long serialVersionUID = 1L;

	@NotNull
	@Size(min = 2,
			max = 80)
	@Column(name = "name",
			length = 80,
			nullable = false)
	private String name;

	@Column(name = "charset")
	private String charset;

	@Column(name = "has_header",
			nullable = false)
	private boolean hasHeader;

	@Column(name = "quote_char",
			nullable = true)
	private Character quoteChar;

	@Column(name = "delimiter_char",
			nullable = false)
	private Character delimiterChar;

	@Column(name = "end_of_line_symbols",
			nullable = false)
	private String endOfLineSymbols;

	@Column(name = "transaction_date_format",
			length = 20,
			nullable = false)
	private String transactionDateFormat;

	@Column(name = "decimal_delimiter",
			nullable = false)
	private Character decimalDelimiter;

	@Column(name = "uniq_id_column_index")
	private Integer uniqIdColumnIndex;

	@Column(name = "description_column_index",
			nullable = false)
	private Integer descriptionColumnIndex;

	@Column(name = "detail_description_column_index")
	private Integer detailDescriptionColumnIndex;

	@Column(name = "account_code_number_column_index")
	private Integer accountCodeNumberColumnIndex;

	@Column(name = "transaction_date_column_index",
			nullable = false)
	private int transactionDateColumnIndex;

	@Column(name = "transaction_type_column_index")
	private Integer transactionTypeColumnIndex;

	@Column(name = "amount_column_index",
			nullable = false)
	private int amountColumnIndex;

	@Column(name = "currency_column_index")
	private Integer currencyColumnIndex;

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(final String charset) {
		this.charset = charset;
	}

	public boolean isHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(final boolean hasHeader) {
		this.hasHeader = hasHeader;
	}

	public Character getQuoteChar() {
		return quoteChar;
	}

	public void setQuoteChar(final Character quoteChar) {
		this.quoteChar = quoteChar;
	}

	public Character getDelimiterChar() {
		return delimiterChar;
	}

	public void setDelimiterChar(final Character delimiterChar) {
		this.delimiterChar = delimiterChar;
	}

	public String getEndOfLineSymbols() {
		return endOfLineSymbols;
	}

	public void setEndOfLineSymbols(final String endOfLineSymbols) {
		this.endOfLineSymbols = endOfLineSymbols;
	}

	public String getTransactionDateFormat() {
		return transactionDateFormat;
	}

	public void setTransactionDateFormat(final String transactionDateFormat) {
		this.transactionDateFormat = transactionDateFormat;
	}

	public Character getDecimalDelimiter() {
		return decimalDelimiter;
	}

	public void setDecimalDelimiter(final Character decimalDelimiter) {
		this.decimalDelimiter = decimalDelimiter;
	}

	public Integer getUniqIdColumnIndex() {
		return uniqIdColumnIndex;
	}

	public void setUniqIdColumnIndex(final Integer uniqIdColumnIndex) {
		this.uniqIdColumnIndex = uniqIdColumnIndex;
	}

	public Integer getDescriptionColumnIndex() {
		return descriptionColumnIndex;
	}

	public void setDescriptionColumnIndex(final Integer descriptionColumnIndex) {
		this.descriptionColumnIndex = descriptionColumnIndex;
	}

	public Integer getDetailDescriptionColumnIndex() {
		return detailDescriptionColumnIndex;
	}

	public void setDetailDescriptionColumnIndex(final Integer detailDescriptionColumnIndex) {
		this.detailDescriptionColumnIndex = detailDescriptionColumnIndex;
	}

	public Integer getAccountCodeNumberColumnIndex() {
		return accountCodeNumberColumnIndex;
	}

	public void setAccountCodeNumberColumnIndex(final Integer accountCodeNumberColumnIndex) {
		this.accountCodeNumberColumnIndex = accountCodeNumberColumnIndex;
	}

	public int getTransactionDateColumnIndex() {
		return transactionDateColumnIndex;
	}

	public void setTransactionDateColumnIndex(final int transactionDateColumnIndex) {
		this.transactionDateColumnIndex = transactionDateColumnIndex;
	}

	public Integer getTransactionTypeColumnIndex() {
		return transactionTypeColumnIndex;
	}

	public void setTransactionTypeColumnIndex(final Integer transactionTypeColumnIndex) {
		this.transactionTypeColumnIndex = transactionTypeColumnIndex;
	}

	public int getAmountColumnIndex() {
		return amountColumnIndex;
	}

	public void setAmountColumnIndex(final int amountColumnIndex) {
		this.amountColumnIndex = amountColumnIndex;
	}

	public Integer getCurrencyColumnIndex() {
		return currencyColumnIndex;
	}

	public void setCurrencyColumnIndex(final Integer currencyColumnIndex) {
		this.currencyColumnIndex = currencyColumnIndex;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		final CashCsvConfig cashCsvConfig = (CashCsvConfig) o;
		if (cashCsvConfig.getId() == null || getId() == null) {
			return false;
		}
		return Objects.equals(getId(), cashCsvConfig.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(getId());
	}

	@Override
	public String toString() {
		return "CashCsvConfig{" + "name='" + name + '\'' + ", charset='" + charset + '\'' + ", hasHeader=" + hasHeader + ", quoteChar=" +
				quoteChar + ", delimiterChar=" + delimiterChar + ", endOfLineSymbols='" + endOfLineSymbols + '\'' +
				", transactionDateFormat='" + transactionDateFormat + '\'' + ", decimalDelimiter=" + decimalDelimiter +
				", uniqIdColumnIndex=" + uniqIdColumnIndex + ", descriptionColumnIndex=" + descriptionColumnIndex +
				", detailDescriptionColumnIndex=" + detailDescriptionColumnIndex + ", accountCodeNumberColumnIndex=" +
				accountCodeNumberColumnIndex + ", transactionDateColumnIndex=" + transactionDateColumnIndex +
				", transactionTypeColumnIndex=" + transactionTypeColumnIndex + ", amountColumnIndex=" + amountColumnIndex +
				", currencyColumnIndex=" + currencyColumnIndex + '}';
	}
}
