package com.enigma.swift_charge_demo.repository;

import com.enigma.swift_charge_demo.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {
    @Query(value = "SELECT c.id, c.customer_name, c.customer_phone, c.user_account_id FROM m_customer AS c " +
            "JOIN m_user_account AS ua ON ua.id = c.user_account_id WHERE c.id = :id AND ua.is_enable = true",nativeQuery = true)
    Optional<Customer> findByIdEnable(String id);

    @Query(value = "SELECT c.id, c.customer_name, c.customer_phone, c.user_account_id FROM m_customer AS c JOIN m_user_account AS ua ON ua.id = c.user_account_id WHERE c.customer_name = :name AND ua.is_enable = true", nativeQuery = true)
    Optional<Page<Customer>> findCustomer(@Param("name") String name, Pageable page);

    @Query(value = "SELECT c.id, c.customer_name, c.customer_phone, c.user_account_id FROM m_customer AS c JOIN m_user_account AS ua ON ua.id = c.user_account_id WHERE ua.is_enable = true", nativeQuery = true)
    Page<Customer> findAllCustomer(Pageable page);
}
