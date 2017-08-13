package com.gagnepain.cashcash.repository;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gagnepain.cashcash.domain.CashSplitSum;
import com.gagnepain.cashcash.domain.User;

/**
 * Spring Data JPA repository for the CashSplitSum entity.
 */
@SuppressWarnings("unused")
public interface CashSplitSumRepository extends JpaRepository<CashSplitSum, Long> {
	@Query("select cashSplitSum from CashSplitSum cashSplitSum where cashSplitSum.user.login = ?#{principal.username}")
	List<CashSplitSum> findByUserIsCurrentUser();

	List<CashSplitSum> findByUserAndAccountIdIn(User user, final Collection<Long> accountIdList);

	List<CashSplitSum> findByUser(User user);

	@Modifying
	@Query(value = "UPDATE cash_split_sum SET amount = amount + :valueToAdd " +
			"WHERE user_id=:userId AND account_id = :cashAccountId AND currency_id = :cashCurrencyId",
			nativeQuery = true)
	int add(@Param("userId") Long userId, @Param("cashAccountId") Long cashAccountId, @Param("cashCurrencyId") Long cashCurrencyId,
			@Param("valueToAdd") BigDecimal valueToAdd);
}
