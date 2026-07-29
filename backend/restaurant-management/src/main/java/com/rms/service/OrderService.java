package com.rms.service;

import com.rms.entity.Customer;
import com.rms.entity.Order;
import com.rms.enums.OrderStatus;
import com.rms.repository.CustomerRepository;
import com.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Order placeOrder(Long customerId) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();

        order.setCustomer(customer);
        order.setTotalAmount(460.0);
        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());

        return orderRepository.save(order);
    }
}