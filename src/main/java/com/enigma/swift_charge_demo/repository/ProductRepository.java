package com.enigma.swift_charge_demo.repository;

import com.enigma.swift_charge_demo.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product,String> {

    @Query(value = "SELECT * FROM m_product WHERE (UPPER(product_name) LIKE UPPER(:name) OR product_price BETWEEN :priceMin AND :priceMax) AND availability = true", nativeQuery = true)
    Optional<Page<Product>> findProductByNameOrPriceBetween(@Param("name") String name, @Param("priceMin") Long priceMin, @Param("priceMax") Long priceMax, Pageable pageable);

    @Query(value = "SELECT * FROM m_product WHERE product_price < :priceMax AND availability = true", nativeQuery = true)
    Optional<Page<Product>> findPriceLess(@Param("priceMax") Long price, Pageable pageable);

    @Query(value = "SELECT * FROM m_product WHERE product_price > :priceMin AND availability = true", nativeQuery = true)
    Optional<Page<Product>> findPriceGreater(@Param("priceMin") Long price, Pageable pageable);

    @Query(value = "SELECT * FROM m_product WHERE availability = true", nativeQuery = true)
    Page<Product> findAllAvailable(Pageable pageable);

    @Query(value = "SELECT * FROM m_product WHERE id = :id AND availability = true", nativeQuery = true)
    Optional<Product> findByIdAvailable(@Param("id") String id);

    @Modifying
    @Query(value = "UPDATE m_product SET availability = false WHERE id = :id", nativeQuery = true)
    void deleteByIdAvailable(@Param("id") String id);
}
