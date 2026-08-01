package com.rms.controller;

import com.rms.dto.AddToCartRequestDTO;
import com.rms.entity.Cart;
import com.rms.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<Cart> addToCart(
            @Valid @RequestBody AddToCartRequestDTO request) {

        return ResponseEntity.ok(cartService.addToCart(request));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<Cart> getCartByCustomerId(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                cartService.getCartByCustomerId(customerId)
        );
    }

    @DeleteMapping("/item/{cartItemId}")
    public ResponseEntity<Cart> removeCartItem(
            @PathVariable Long cartItemId) {

        return ResponseEntity.ok(
                cartService.removeCartItem(cartItemId)
        );
    }

    @DeleteMapping("/clear/{customerId}")
    public ResponseEntity<Cart> clearCart(
            @PathVariable Long customerId) {

        return ResponseEntity.ok(
                cartService.clearCart(customerId)
        );
    }
}