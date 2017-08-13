package com.gagnepain.cashcash.service;

import java.util.List;
import javax.inject.Inject;

import com.gagnepain.cashcash.domain.CashOwnedResource;
import com.gagnepain.cashcash.domain.User;

/**
 * Util to impact generic resource
 */
public class AbstractCashResourceService<T extends CashOwnedResource> {
	@Inject
	protected UserService userService;

	public void reconnectUser(final T resource) {
		// Reconnect the user
		final User userWithAuthorities = userService.getUserWithoutAuthorities();

		resource.getOwnedResources()
				.stream()
				.forEach(r -> r.setUser(userWithAuthorities));
	}

	public void reconnectUser(final List<T> resources) {
		// Reconnect the user
		final User userWithAuthorities = userService.getUserWithoutAuthorities();

		resources.stream()
				.flatMap(r -> r.getOwnedResources()
						.stream())
				.forEach(r -> r.setUser(userWithAuthorities));
	}
}
