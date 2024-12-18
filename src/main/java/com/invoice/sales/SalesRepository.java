package com.invoice.sales;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

@Repository
public interface SalesRepository extends JpaRepository<Sales, Long> {

    // Modify the native query to handle list of Sales and batch insert them
    // efficiently
    @Modifying
    @Query(value = "INSERT INTO sales (user_id, location_id, product_id, amount, date) VALUES (:userId, :locationId, :productId, :amount, :date)", nativeQuery = true)
    void bulkInsert(
            @Param("userId") Long userId,
            @Param("locationId") Long locationId,
            @Param("productId") Long productId,
            @Param("amount") BigDecimal amount,
            @Param("date") LocalDate date);

    // Bulk insert using a list of Sales, where you will insert them in batches
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO sales (user_id, location_id, product_id, amount, date) " +
            "VALUES (:#{#salesList[0].userId}, :#{#salesList[0].locationId}, :#{#salesList[0].productId}, :#{#salesList[0].amount}, :#{#salesList[0].date})", nativeQuery = true)
    void bulkInsertList(@Param("salesList") List<Sales> salesList);
}