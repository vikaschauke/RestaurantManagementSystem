package com.rms.service;

import com.rms.dto.OrderRequestDTO;
import com.rms.entity.Customer;
import com.rms.entity.Order;
import com.rms.enums.OrderStatus;
import com.rms.repository.CustomerRepository;
import com.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rms.dto.OrderItemRequestDTO;
import com.rms.dto.OrderRequestDTO;
import com.rms.entity.FoodItem;
import com.rms.entity.OrderItem;
import com.rms.repository.FoodItemRepository;
import com.rms.repository.OrderItemRepository;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Order placeOrder(Long customerId, OrderRequestDTO request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Order order = new Order();

        order.setCustomer(customer);
        order.setStatus(OrderStatus.PLACED);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(0.0);

        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();
        double totalAmount = 0.0;

        for (OrderItemRequestDTO itemRequest : request.getItems()) {

            FoodItem foodItem = foodItemRepository
                    .findById(itemRequest.getFoodItemId())
                    .orElseThrow(() -> new RuntimeException("Food item not found"));

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setFoodItem(foodItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(foodItem.getPrice());

            double itemTotal =
                    foodItem.getPrice() * itemRequest.getQuantity();

            totalAmount += itemTotal;

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalAmount(totalAmount);

        return orderRepository.save(savedOrder);
    }

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;
}