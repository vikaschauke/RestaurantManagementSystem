package com.rms.repository;

import com.rms.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);

    Order findTopByCustomerIdOrderByOrderDateDesc(Long customerId);

    @Query("SELECT SUM(o.totalAmount) FROM Order o")
    Double getTotalRevenue();

}

