package com.gagnepain.cashcash.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gagnepain.cashcash.domain.CashCsvConfig;
import com.gagnepain.cashcash.domain.User;

/**
 * Spring Data JPA repository for the CashCsvConfig entity.
 */
@SuppressWarnings("unused")
public interface CashCsvConfigRepository extends JpaRepository<CashCsvConfig, Long> {
	CashCsvConfig findOneByIdAndUser( Long id, User user);

	CashCsvConfig findOneByUserAndName(User user, String name);

	Page<CashCsvConfig> findByUser(User user, Pageable pageable);

	Long countByUser(User user);
}
