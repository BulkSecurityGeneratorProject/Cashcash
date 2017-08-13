package com.gagnepain.cashcash.service;

import java.time.ZonedDateTime;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.CashRate;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.repository.CashRateRepository;
import com.gagnepain.cashcash.service.util.SupportedCurrencies;
import com.gagnepain.cashcash.web.rest.dto.ExternalCashRateDTO;

/**
 * Service Implementation for managing CashRate.
 */
@Service
@Transactional
public class CashRateService {
	@Inject
	private CashRateRepository cashRateRepository;

	@Inject
	private ExternalCashRateService externalCashRateService;

	@Value("${cashcash.dayNbBeforeUpdateCashRate}")
	private int DAYS_BEFORE_UPDATE;

	/**
	 * Get one cashRate by code.
	 *
	 * @param code
	 * 		the code of the entity
	 *
	 * @return the entity
	 */
	@Transactional(readOnly = true)
	public CashRate findOne(final String code) throws BusinessException {
		CashRate cashRateInDB = cashRateRepository.findOneByCode(code);

		if (cashRateInDB == null || cashRateInDB.getModifiedDate()
				.plusDays(DAYS_BEFORE_UPDATE)
				.isBefore(ZonedDateTime.now())) {
			return null;
		} else {
			return cashRateInDB;
		}
	}

	public CashRate updateOrCreate(final String code) throws BusinessException {
		if (SupportedCurrencies.isSupportedCurrency(code)) {
			ExternalCashRateDTO externalCashRateDTO = externalCashRateService.getCashRate(code);

			CashRate cashRateInDB = cashRateRepository.findOneByCode(code);
			if (cashRateInDB == null) {
				cashRateInDB = new CashRate();
			}
			cashRateInDB.setCode(code);
			cashRateInDB.setRates(externalCashRateDTO.getRates());

			cashRateRepository.save(cashRateInDB);
			return cashRateInDB;
		}

		return null;
	}
}
