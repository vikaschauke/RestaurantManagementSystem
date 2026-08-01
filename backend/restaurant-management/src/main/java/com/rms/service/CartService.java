package com.rms.service;

import com.rms.dto.AddToCartRequestDTO;
import com.rms.entity.Cart;
import com.rms.entity.CartItem;
import com.rms.entity.Customer;
import com.rms.entity.FoodItem;
import com.rms.repository.CartItemRepository;
import com.rms.repository.CartRepository;
import com.rms.repository.CustomerRepository;
import com.rms.repository.FoodItemRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CustomerRepository customerRepository;
    private final FoodItemRepository foodItemRepository;

    public CartService(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            CustomerRepository customerRepository,
            FoodItemRepository foodItemRepository) {

        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.customerRepository = customerRepository;
        this.foodItemRepository = foodItemRepository;
    }

    public Cart addToCart(AddToCartRequestDTO request) {

        Customer customer = customerRepository
                .findById(request.getCustomerId())
                .orElseThrow(() ->
                        new RuntimeException("Customer not found")
                );

        FoodItem foodItem = foodItemRepository
                .findById(request.getFoodItemId())
                .orElseThrow(() ->
                        new RuntimeException("Food item not found")
                );

        Cart cart = cartRepository
                .findByCustomerId(request.getCustomerId())
                .orElseGet(() -> {

                    Cart newCart = new Cart();
                    newCart.setCustomer(customer);
                    newCart.setTotalAmount(0.0);

                    return cartRepository.save(newCart);
                });

        CartItem cartItem = cartItemRepository
                .findByCartIdAndFoodItemId(
                        cart.getId(),
                        foodItem.getId()
                )
                .orElse(null);

        if (cartItem != null) {

            cartItem.setQuantity(
                    cartItem.getQuantity() + request.getQuantity()
            );

        } else {

            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setFoodItem(foodItem);
            cartItem.setQuantity(request.getQuantity());

            cart.getCartItems().add(cartItem);
        }

        cartItem.setSubTotal(
                foodItem.getPrice() * cartItem.getQuantity()
        );

        cartItemRepository.save(cartItem);

        double totalAmount = cart.getCartItems()
                .stream()
                .mapToDouble(CartItem::getSubTotal)
                .sum();

        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);
    }

    public Cart getCartByCustomerId(Long customerId) {

        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public Cart removeCartItem(Long cartItemId) {

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        Cart cart = cartItem.getCart();

        cart.getCartItems().remove(cartItem);

        cartItemRepository.delete(cartItem);

        double totalAmount = cart.getCartItems()
                .stream()
                .mapToDouble(CartItem::getSubTotal)
                .sum();

        cart.setTotalAmount(totalAmount);

        return cartRepository.save(cart);
    }

    public Cart clearCart(Long customerId) {

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getCartItems().clear();

        cart.setTotalAmount(0.0);

        return cartRepository.save(cart);
    }
}