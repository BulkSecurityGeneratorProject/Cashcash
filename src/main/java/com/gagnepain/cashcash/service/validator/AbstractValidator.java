package com.gagnepain.cashcash.service.validator;

import java.util.Objects;
import java.util.Optional;

import com.gagnepain.cashcash.domain.CashOwnedResource;
import com.gagnepain.cashcash.domain.CashResource;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Abstract validator that gather all the common validation
 */
public abstract class AbstractValidator<T extends CashOwnedResource> {
	public Optional<CashError> validateForCreation(final T cashResource) {

		if (cashResource.getId() != null) {
			return Optional.of(new CashError("idexists", "A new resource cannot already have an ID"));
		}

		return Optional.empty();
	}

	public Optional<CashError> validateForUpdate(final T cashResource) {

		final T cashResourceInDB = findResourceByUserAndId(cashResource.getUser(), cashResource.getId());

		if (!cashResource.getUser()
				.equals(cashResourceInDB.getUser())) {
			return Optional.of(new CashError("differentUser", "User can't be changed"));
		}

		return Optional.empty();
	}

	protected Optional<CashError> validateForUpdate(final T updatedCashResource, final T oldCashResource) {
		if (oldCashResource == null) {
			return Optional.of(new CashError("idDoesNotExist", "This resource doesn't exist"));
		}

		if (!Objects.equals(updatedCashResource.getUser(), oldCashResource.getUser())) {
			return Optional.of(new CashError("userId", "It's not possible to change the userId"));
		}

		return Optional.empty();
	}

	public Optional<CashError> validateForRetrieve(final T cashResource) {
		if (cashResource == null) {
			return Optional.of(new CashError("idDoesNotExist", "This resource doesn't exist"));
		}

		//No error
		return Optional.empty();
	}

	public Optional<CashError> validateForDeletion(final T cashResource) {
		if (cashResource == null) {
			return Optional.of(new CashError("idDoesNotExist", "This resource doesn't exist"));
		}

		//No error
		return Optional.empty();
	}

	abstract T findResourceByUserAndId(final User user, final Long id);
}
