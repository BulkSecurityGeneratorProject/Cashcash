package com.gagnepain.cashcash.service;

import java.util.Optional;
import javax.inject.Inject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gagnepain.cashcash.domain.CashCsvConfig;
import com.gagnepain.cashcash.domain.User;
import com.gagnepain.cashcash.domain.exception.BusinessException;
import com.gagnepain.cashcash.repository.CashCsvConfigRepository;
import com.gagnepain.cashcash.service.validator.CashCsvConfigValidator;
import com.gagnepain.cashcash.web.rest.errors.CashError;

/**
 * Service Implementation for managing CashCurrency.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CashCsvConfigService extends AbstractCashResourceService<CashCsvConfig> {
	@Inject
	private CashCsvConfigRepository cashCsvConfigRepository;

	@Inject
	private CashCsvConfigValidator cashCsvConfigValidator;

	@Inject
	private UserService userService;

	/**
	 * Create a new CashCsvConfig
	 */
	public CashCsvConfig create(final CashCsvConfig cashCsvConfig) throws BusinessException {
		reconnectUser(cashCsvConfig);
		final Optional<CashError> errorOpt = cashCsvConfigValidator.validateForCreation(cashCsvConfig);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		return cashCsvConfigRepository.save(cashCsvConfig);
	}

	/**
	 * Update CashCsvConfig
	 */
	public CashCsvConfig update(final CashCsvConfig cashCsvConfig) throws BusinessException {
		reconnectUser(cashCsvConfig);
		final Optional<CashError> errorOpt = cashCsvConfigValidator.validateForUpdate(cashCsvConfig);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}

		return cashCsvConfigRepository.save(cashCsvConfig);
	}

	/**
	 * Get all the CashCsvConfigs.
	 *
	 * @return the list of entities
	 */
	@Transactional(readOnly = true)
	public Page<CashCsvConfig> findAll(final Pageable pageable) {
		final User loggedUser = userService.getUserWithoutAuthorities();
		return cashCsvConfigRepository.findByUser(loggedUser, pageable);
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
	public CashCsvConfig findOne(final Long id) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashCsvConfig cashCsvConfig = cashCsvConfigRepository.findOneByIdAndUser(id, user);
		// We first have to retrieve it in order to validate the retrieved resource
		final Optional<CashError> errorOpt = cashCsvConfigValidator.validateForRetrieve(cashCsvConfig);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}
		return cashCsvConfig;
	}

	/**
	 * Delete the  cashCurrency by id.
	 *
	 * @param id
	 * 		the id of the entity
	 */
	public void delete(final Long id) throws BusinessException {
		final User user = userService.getUserWithoutAuthorities();
		final CashCsvConfig cashCsvConfig = cashCsvConfigRepository.findOneByIdAndUser(id, user);

		// We first have to retrieve it in order to validate the delete of the resource
		final Optional<CashError> errorOpt = cashCsvConfigValidator.validateForDeletion(cashCsvConfig);
		if (errorOpt.isPresent()) {
			final CashError cashError = errorOpt.get();
			throw new BusinessException(cashError.getErrorKey(), cashError.getMessage());
		}
		cashCsvConfigRepository.delete(id);
	}
}
