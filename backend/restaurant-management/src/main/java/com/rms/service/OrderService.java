package com.rms.service;

import com.rms.dto.OrderItemRequestDTO;
import com.rms.dto.OrderRequestDTO;
import com.rms.entity.Cart;
import com.rms.entity.CartItem;
import com.rms.entity.Customer;
import com.rms.entity.FoodItem;
import com.rms.entity.Order;
import com.rms.entity.OrderItem;
import com.rms.enums.OrderStatus;
import com.rms.exception.OrderNotFoundException;
import com.rms.repository.CartItemRepository;
import com.rms.repository.CartRepository;
import com.rms.repository.CustomerRepository;
import com.rms.repository.FoodItemRepository;
import com.rms.repository.OrderItemRepository;
import com.rms.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;


    // Existing method:
    // Direct request se Order create karta hai
    public Order placeOrder(Long customerId, OrderRequestDTO request) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new RuntimeException("Customer not found")
                );

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
                    .orElseThrow(() ->
                            new RuntimeException("Food item not found")
                    );

            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(savedOrder);
            orderItem.setFoodItem(foodItem);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(foodItem.getPrice());

            double itemTotal =
                    foodItem.getPrice()
                            * itemRequest.getQuantity();

            totalAmount += itemTotal;

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);

        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalAmount(totalAmount);

        return orderRepository.save(savedOrder);
    }


    // New Day 18 method:
    // Cart ko final Order me convert karta hai
    @Transactional
    public Order checkoutCart(Long customerId) {

        // Step 1: Customer ka cart find karo
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Cart not found for customer ID: "
                                        + customerId
                        )
                );

        // Step 2: Cart empty hai ya nahi check karo
        if (cart.getCartItems() == null
                || cart.getCartItems().isEmpty()) {

            throw new RuntimeException("Cart is empty");
        }

        // Step 3: New Order object create karo
        Order order = new Order();

        // Step 4: Cart wale customer ko Order me set karo
        order.setCustomer(cart.getCustomer());

        // Step 5: Initial Order status set karo
        order.setStatus(OrderStatus.PLACED);

        // Step 6: Current date aur time set karo
        order.setOrderDate(LocalDateTime.now());

        // Starting me total amount zero rakho
        order.setTotalAmount(0.0);

        // Step 7: Order ko pehli baar save karo
        // Isse Order ko database ID mil jayegi
        Order savedOrder = orderRepository.save(order);

        List<OrderItem> orderItems = new ArrayList<>();

        double totalAmount = 0.0;

        // Cart items ki copy bana rahe hain
        // Taaki delete karte waqt list modification problem na aaye
        List<CartItem> cartItems =
                new ArrayList<>(cart.getCartItems());

        // Step 8: Har CartItem ko OrderItem me convert karo
        for (CartItem cartItem : cartItems) {

            FoodItem foodItem = cartItem.getFoodItem();

            OrderItem orderItem = new OrderItem();

            // Ye item kis Order ka part hai
            orderItem.setOrder(savedOrder);

            // Customer ne kaunsi dish order ki
            orderItem.setFoodItem(foodItem);

            // Customer ne kitni quantity order ki
            orderItem.setQuantity(cartItem.getQuantity());

            // Checkout ke time ka price save karo
            orderItem.setPrice(foodItem.getPrice());

            // Item ka total calculate karo
            double itemTotal =
                    foodItem.getPrice()
                            * cartItem.getQuantity();

            // Final total me add karo
            totalAmount += itemTotal;

            // OrderItem ko list me add karo
            orderItems.add(orderItem);
        }

        // Step 9: Saare OrderItems database me save karo
        orderItemRepository.saveAll(orderItems);

        // Step 10: Order ke andar items aur total set karo
        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalAmount(totalAmount);

        // Final Order update karke save karo
        Order finalOrder = orderRepository.save(savedOrder);

        // Step 11: Checkout ke baad CartItems delete karo
        cartItemRepository.deleteAll(cartItems);

        // Java object ke andar bhi list clear karo
        cart.getCartItems().clear();

        // Cart total zero karo
        cart.setTotalAmount(0.0);

        // Empty cart save karo
        cartRepository.save(cart);

        // Step 12: Created Order return karo
        return finalOrder;
    }


    public Order updateOrderStatus(
            Long orderId,
            OrderStatus status
    ) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with ID: "
                                        + orderId
                        )
                );

        order.setStatus(status);

        return orderRepository.save(order);
    }


    public List<Order> getAllOrders() {

        return orderRepository.findAll();
    }


    public List<Order> getOrdersByCustomer(
            Long customerId
    ) {

        return orderRepository
                .findByCustomerId(customerId);
    }


    public Order getOrderById(Long orderId) {

        return orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException(
                                "Order not found with ID: "
                                        + orderId
                        )
                );
    }
}