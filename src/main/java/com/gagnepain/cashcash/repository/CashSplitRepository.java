package com.gagnepain.cashcash.repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.CashSplit;
import com.gagnepain.cashcash.domain.User;

/**
 * Spring Data JPA repository for the CashSplit entity.
 */
@SuppressWarnings("unused")
public interface CashSplitRepository extends JpaRepository<CashSplit, Long> {
	@Query("select cashSplit from CashSplit cashSplit where cashSplit.user.login = ?#{principal.username}")
	List<CashSplit> findByUserIsCurrentUser();

	CashSplit findFirstByAccount(final CashAccount account);

	CashSplit findOneByIdAndUser(Long id, User user);

	Page<CashSplit> findByUserAndTransactionDateBetweenOrderByTransactionDateAsc(final User user, final ZonedDateTime startDate,
			final ZonedDateTime endDate, final Pageable pageable);

	Page<CashSplit> findByUserAndAccountIdInAndTransactionDateBetween(final User user,
			final List<Long> accountIdList, final ZonedDateTime startDate, final ZonedDateTime endDate, final Pageable pageable);

	@Query(value = "SELECT account_id, currency_id, COALESCE(SUM(amount),0) FROM cash_split " +
			"WHERE user_id=:userId " +
			"GROUP BY account_id, currency_id",
			nativeQuery = true)
	List<Object[]> getSum(@Param("userId") Long userId);

	@Query(value = "SELECT account_id, currency_id, COALESCE(SUM(amount),0) FROM cash_split " +
			"WHERE user_id=:userId AND account_id IN (:accountIdList) " +
			"GROUP BY account_id, currency_id",
			nativeQuery = true)
	List<Object[]> getSum(@Param("userId") Long userId, @Param("accountIdList") List<Long> accountIdList);

	@Query(value = "SELECT COALESCE(SUM(amount),0) FROM cash_split " +
			"WHERE user_id=:userId AND account_id=:cashAccountId AND currency_id=:cashCurrencyId",
			nativeQuery = true)
	BigDecimal getSum(@Param("userId") Long userId, @Param("cashAccountId") Long cashAccountId,
			@Param("cashCurrencyId") Long cashCurrencyId);

	@Query(value = "SELECT account_id, currency_id, COALESCE(SUM(amount),0) FROM cash_split " +
			"WHERE user_id=:userId AND transaction_date >= :fromDate " +
			"GROUP BY account_id, currency_id",
			nativeQuery = true)
	List<Object[]> getSum(@Param("userId") Long userId, @Param("fromDate") final Date fromDate);

	@Query(value = "SELECT account_id, currency_id, COALESCE(SUM(amount),0) FROM cash_split " +
			"WHERE user_id=:userId AND transaction_date >= :fromDate AND account_id IN (:accountIdList)" +
			"GROUP BY account_id, currency_id",
			nativeQuery = true)
	List<Object[]> getSum(@Param("userId") Long userId, @Param("accountIdList") List<Long> accountIdList,
			@Param("fromDate") final Date fromDate);
}
