package com.gagnepain.cashcash.service.validator;

import java.util.Optional;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gagnepain.cashcash.domain.CashCsvConfig;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.repository.CashCsvConfigRepository;
import com.gagnepain.cashcash.service.UserService;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Class which suppose to validate the cashCurrency resource
 */
@Component
public class CashCsvConfigValidator extends AbstractValidator<CashCsvConfig> {
	@Inject
	private CashCsvConfigRepository cashCsvConfigRepository;

	@Inject
	private UserService userService;

	@Value("${cashcash.maxCashCsvConfigNb}")
	private int MAX_CASH_CSV_CONFIG_NB;

	public Optional<CashError> validateForCreation(final CashCsvConfig cashCsvConfig) {
		final User loggedUser = userService.getUserWithoutAuthorities();
		long existingCsvConfigNb = cashCsvConfigRepository.countByUser(loggedUser);

		if (existingCsvConfigNb + 1 > MAX_CASH_CSV_CONFIG_NB) {
			return Optional.of(
					new CashError("tooMuchCashCsvConfig", "Impossible to have more than " + MAX_CASH_CSV_CONFIG_NB + " csv configs."));
		}

		return super.validateForCreation(cashCsvConfig);
	}

	@Override
	CashCsvConfig findResourceByUserAndId(final User user, final Long id) {
		return cashCsvConfigRepository.findOneByIdAndUser(id, user);
	}
}
