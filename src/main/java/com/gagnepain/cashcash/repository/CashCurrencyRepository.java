package com.gagnepain.cashcash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gagnepain.cashcash.domain.CashCurrency;
import com.gagnepain.cashcash.domain.User;

/**
 * Spring Data JPA repository for the CashCurrency entity.
 */
@SuppressWarnings("unused")
public interface CashCurrencyRepository extends JpaRepository<CashCurrency, Long> {
	CashCurrency findOneByCode(String code);
}
