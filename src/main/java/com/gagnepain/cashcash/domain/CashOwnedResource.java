package com.gagnepain.cashcash.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * An abstract resource owned by a user
 */
@MappedSuperclass
public abstract class CashOwnedResource extends CashResource {
	@ManyToOne(fetch = FetchType.LAZY)
	@NotNull
	private User user;

	public CashOwnedResource() {
	}

	public CashOwnedResource(final CashOwnedResource cashOwnedResource) {
		super(cashOwnedResource);
		this.user = cashOwnedResource.user;
	}

	/**
	 * Return all the cashOwnedResource contained in one cashOwnedResource including itself
	 *
	 * @return this and all subMember which are cashOwnedResource
	 */
	@JsonIgnore
	public List<CashOwnedResource> getOwnedResources() {
		final List<CashOwnedResource> cashResources = new ArrayList<>();
		cashResources.add(this);
		return cashResources;
	}

	public User getUser() {
		return user;
	}

	public void setUser(final User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this).appendSuper(super.toString())
				.append("user", user)
				.toString();
	}
}
