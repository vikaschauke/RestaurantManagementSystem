package com.rms.service;

import com.rms.dto.DashboardResponseDTO;
import com.rms.repository.CustomerRepository;
import com.rms.repository.FoodItemRepository;
import com.rms.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public DashboardResponseDTO getDashboardData() {

        Long totalCustomers = customerRepository.count();

        Long totalFoodItems = foodItemRepository.count();

        Long totalOrders = orderRepository.count();

        Double totalRevenue = orderRepository.getTotalRevenue();

        if (totalRevenue == null) {
            totalRevenue = 0.0;
        }

        DashboardResponseDTO dashboardResponseDTO =
                new DashboardResponseDTO();

        dashboardResponseDTO.setTotalCustomers(totalCustomers);
        dashboardResponseDTO.setTotalFoodItems(totalFoodItems);
        dashboardResponseDTO.setTotalOrders(totalOrders);
        dashboardResponseDTO.setTotalRevenue(totalRevenue);

        return dashboardResponseDTO;
    }
}