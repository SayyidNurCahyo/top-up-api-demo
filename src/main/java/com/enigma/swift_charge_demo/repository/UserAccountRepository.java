package com.enigma.swift_charge_demo.repository;

import com.enigma.swift_charge_demo.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
public interface UserAccountRepository extends JpaRepository<UserAccount, String> {
    @Query(value = "SELECT * FROM m_user_account WHERE email = :email", nativeQuery = true)
    Optional<UserAccount> findByEmail(@Param("email") String email);

    @Modifying
    @Query(value = "UPDATE m_user_account SET is_enable = false WHERE id = :id", nativeQuery = true)
    void disableAccount(@Param("id") String id);
}
