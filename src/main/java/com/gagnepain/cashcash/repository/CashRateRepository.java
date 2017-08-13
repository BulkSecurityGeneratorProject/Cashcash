package com.gagnepain.cashcash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.CashRate;

/**
 * Spring Data JPA repository for the CashRate entity.
 */
@SuppressWarnings("unused")
public interface CashRateRepository extends JpaRepository<CashRate, Long> {
	CashRate findOneByCode(String code);
}
