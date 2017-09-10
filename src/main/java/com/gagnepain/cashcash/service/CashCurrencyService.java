package com.gagnepain.cashcash.service;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

import org.apache.commons.lang3.EnumUtils;
import org.joda.money.CurrencyUnit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.repository.CashCurrencyRepository;
import com.gagnepain.cashcash.service.util.SupportedCurrencies;

/**
 * Service Implementation for managing CashCurrency.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CashCurrencyService {
	@Inject
	private CashCurrencyRepository cashCurrencyRepository;

	/**
	 * Create a new cashCurrency
	 */
	public CashCurrency create(final CashCurrency cashCurrency) throws BusinessException {
		return cashCurrencyRepository.save(cashCurrency);
	}

	/**
	 * Update cashAccounts
	 */
	public CashCurrency update(final CashCurrency cashCurrency) throws BusinessException {
		return cashCurrencyRepository.save(cashCurrency);
	}

	/**
	 * Get all the cashCurrencies.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public List<CashCurrency> findAll() {
		final List<CashCurrency> result = cashCurrencyRepository.findAll();
		return result;
	}

	/**
	 * Get one cashCurrency by id.
	 *
	 * @param id
	 * 		the id of the entity
	 *
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CashCurrency findOne(final Long id) throws BusinessException {
		final CashCurrency cashCurrency = cashCurrencyRepository.findOne(id);
		return cashCurrency;
	}

	/**
	 * Get one cashCurrency by id.
	 *
	 * @param code
	 * 		the code of the entity
	 *
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CashCurrency findOne(final String code) {
		return cashCurrencyRepository.findOneByCode(code);
	}

	/**
	 * Delete the  cashCurrency by id.
	 *
	 * @param id
	 * 		the id of the entity
	 */
	public void delete(final Long id) throws BusinessException {
		final CashCurrency cashCurrency = cashCurrencyRepository.findOne(id);
		cashCurrencyRepository.delete(id);
	}

	public void createCurrencies() {
		final List<CashCurrency> currenciesToSave = new ArrayList<>();

		for (final CurrencyUnit currencyUnit : CurrencyUnit.registeredCurrencies()) {
			if (SupportedCurrencies.isSupportedCurrency(currencyUnit.getCode())) {
				final CashCurrency cashCurrency = new CashCurrency();
				cashCurrency.setCode(currencyUnit.getCode());
				cashCurrency.setNumericCode(currencyUnit.getNumericCode());
				currenciesToSave.add(cashCurrency);
			}
		}

		cashCurrencyRepository.save(currenciesToSave);
	}
}
