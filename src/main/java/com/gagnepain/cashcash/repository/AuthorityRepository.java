package com.gagnepain.cashcash.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gagnepain.cashcash.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {
}
