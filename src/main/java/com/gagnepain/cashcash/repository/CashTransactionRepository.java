package com.gagnepain.cashcash.repository;

import java.time.ZonedDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gagnepain.cashcash.domain.CashTransaction;
import com.gagnepain.cashcash.domain.User;

/**
 * Spring Data JPA repository for the CashTransaction entity.
 */
@SuppressWarnings("unused")
public interface CashTransactionRepository extends JpaRepository<CashTransaction, Long> {
	@Query("select cashTransaction from CashTransaction cashTransaction where cashTransaction.user.login = ?#{principal.username}")
	List<CashTransaction> findByUserIsCurrentUser();

	CashTransaction findOneByIdAndUser(Long id, User user);

	List<CashTransaction> findTop200ByUserOrderByTransactionDateDesc(User user);

	Page<CashTransaction> findByUserAndTransactionDateBetween(User user, ZonedDateTime startDate, ZonedDateTime endDate, Pageable pageable);

	Page<CashTransaction> findByUserAndTransactionDateBetweenAndSplitsAccountIdIn(User user, ZonedDateTime startDate, ZonedDateTime endDate,
			List<Long> accountIds, Pageable pageable);
}
