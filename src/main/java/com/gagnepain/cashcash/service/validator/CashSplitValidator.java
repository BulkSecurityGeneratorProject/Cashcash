package com.gagnepain.cashcash.service.validator;

import javax.inject.Inject;

import org.springframework.stereotype.Component;

import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.repository.CashSplitRepository;

/**
 * Class which suppose to validate the cashDebitCredit resource
 */
@Component
public class CashSplitValidator extends AbstractValidator<CashSplit> {
	@Inject
	private CashSplitRepository cashSplitRepository;

	@Override
	CashSplit findResourceByUserAndId(final User user, final Long id) {
		return cashSplitRepository.findOneByIdAndUser(id, user);
	}
}
