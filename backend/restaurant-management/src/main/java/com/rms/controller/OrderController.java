package com.rms.controller;

import com.rms.dto.OrderRequestDTO;
import com.rms.dto.OrderResponseDTO;
import com.rms.enums.OrderStatus;
import com.rms.service.OrderService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/{customerId}")
    public ResponseEntity<OrderResponseDTO> placeOrder(
            @PathVariable Long customerId,
            @RequestBody OrderRequestDTO request) {

        return ResponseEntity.ok(
                orderService.placeOrder(customerId, request)
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam OrderStatus status) {

        return ResponseEntity.ok(
                orderService.updateOrderStatus(orderId, status)
        );
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {

        return ResponseEntity.ok(
                orderService.getAllOrders()
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderResponseDTO>> getOrdersByCustomer(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                orderService.getOrdersByCustomer(customerId)
        );
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long orderId) {

        return ResponseEntity.ok(
                orderService.getOrderById(orderId)
        );
    }

    @PostMapping("/checkout/{customerId}")
    public ResponseEntity<OrderResponseDTO> checkoutCart(
            @PathVariable Long customerId) {

        OrderResponseDTO order =
                orderService.checkoutCart(customerId);

        return ResponseEntity.ok(order);
    }
}