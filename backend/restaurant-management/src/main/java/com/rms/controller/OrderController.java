package com.rms.controller;

import com.rms.dto.OrderRequestDTO;
import com.rms.entity.Order;
import com.rms.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Order> placeOrder(
            @PathVariable Long customerId,
            @RequestBody OrderRequestDTO request) {

        return ResponseEntity.ok(orderService.placeOrder(customerId, request));
    }
}