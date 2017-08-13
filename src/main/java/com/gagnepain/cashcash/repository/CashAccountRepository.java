package com.gagnepain.cashcash.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.gagnepain.cashcash.domain.CashAccount;
import com.gagnepain.cashcash.domain.User;

/**
 * Spring Data JPA repository for the CashAccount entity.
 */
@SuppressWarnings("unused")
public interface CashAccountRepository extends JpaRepository<CashAccount, Long> {
	@Query("select cashAccount from CashAccount cashAccount where cashAccount.user.login = ?#{principal.username}")
	List<CashAccount> findByUserIsCurrentUser();

	CashAccount findByUserAndAccountNumber(User user, String accountNumber);

	List<CashAccount> findByUser(User user);

	CashAccount findOneByIdAndUser(Long id, User user);

	List<CashAccount> findByUserAndParentAccount(User user, CashAccount parentAccount);

	Long countByUser(User user);
}
