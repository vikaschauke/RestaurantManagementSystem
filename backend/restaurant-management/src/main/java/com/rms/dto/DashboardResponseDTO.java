package com.rms.dto;

public class DashboardResponseDTO {

    private Long totalCustomers;
    private Long totalFoodItems;
    private Long totalOrders;
    private Double totalRevenue;

    public DashboardResponseDTO() {
    }

    public DashboardResponseDTO(
            Long totalCustomers,
            Long totalFoodItems,
            Long totalOrders,
            Double totalRevenue) {

        this.totalCustomers = totalCustomers;
        this.totalFoodItems = totalFoodItems;
        this.totalOrders = totalOrders;
        this.totalRevenue = totalRevenue;
    }

    public Long getTotalCustomers() {
        return totalCustomers;
    }

    public void setTotalCustomers(Long totalCustomers) {
        this.totalCustomers = totalCustomers;
    }

    public Long getTotalFoodItems() {
        return totalFoodItems;
    }

    public void setTotalFoodItems(Long totalFoodItems) {
        this.totalFoodItems = totalFoodItems;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(Double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}