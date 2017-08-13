package com.gagnepain.cashcash.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gagnepain.cashcash.repository.custom.annotation.FunctionCreationTimestamp;
import com.gagnepain.cashcash.repository.custom.annotation.FunctionUpdateTimestamp;

/**
 * An abstract resource
 */
@MappedSuperclass
public abstract class CashResource implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "creation_date")
	@FunctionCreationTimestamp
	private ZonedDateTime creationDate;

	@Column(name = "modified_date")
	@FunctionUpdateTimestamp
	private ZonedDateTime modifiedDate;


	public CashResource() {
	}

	public CashResource(final CashResource cashResource) {
		this.id = cashResource.id;
		this.creationDate = cashResource.creationDate;
		this.modifiedDate = cashResource.modifiedDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public ZonedDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(final ZonedDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public ZonedDateTime getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(final ZonedDateTime modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final CashResource that = (CashResource) o;

		if (creationDate != null ? !creationDate.equals(that.creationDate) : that.creationDate != null) {
			return false;
		}
		if (id != null ? !id.equals(that.id) : that.id != null) {
			return false;
		}
		if (modifiedDate != null ? !modifiedDate.equals(that.modifiedDate) : that.modifiedDate != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
		result = 31 * result + (modifiedDate != null ? modifiedDate.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
				.append("creationDate", creationDate)
				.append("modifiedDate", modifiedDate)
				.toString();
	}
}
