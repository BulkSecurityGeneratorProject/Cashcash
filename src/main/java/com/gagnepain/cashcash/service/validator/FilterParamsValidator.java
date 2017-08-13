package com.gagnepain.cashcash.service.validator;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.gagnepain.cashcash.domain.FilterParams;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Validator for the FilterParams Object
 */
@Component
public class FilterParamsValidator {
	public Optional<CashError> validate(final FilterParams filterParams) {
		return Optional.empty();
	}
}
