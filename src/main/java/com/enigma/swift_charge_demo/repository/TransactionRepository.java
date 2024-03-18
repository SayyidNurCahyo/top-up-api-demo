package com.enigma.swift_charge_demo.repository;

import com.enigma.swift_charge_demo.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction,String> {
    @Query(value = "SELECT * FROM t_bill WHERE transaction_date BETWEEN :dateStart AND :dateEnd", nativeQuery = true)
    Optional<Page<Transaction>> findBetweenDate(@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd, Pageable pageable);

    @Query(value = "SELECT * FROM t_bill WHERE transaction_date < :dateEnd", nativeQuery = true)
    Optional<Page<Transaction>> findBeforeDate(@Param("dateEnd") Date dateEnd, Pageable pageable);

    @Query(value = "SELECT * FROM t_bill WHERE transaction_date > :dateStart", nativeQuery = true)
    Optional<Page<Transaction>> findAfterDate(@Param("dateStart") Date dateEnd, Pageable pageable);

    @Query(value = "SELECT * FROM t_bill WHERE customer_id = :id", nativeQuery = true)
    Optional<List<Transaction>> findTrById(@Param("id") String customerId);
}
