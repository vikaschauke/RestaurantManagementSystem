package com.rms.dto;

public class OrderItemRequestDTO {

    private Long foodItemId;
    private Integer quantity;

    public OrderItemRequestDTO() {
    }

    public OrderItemRequestDTO(Long foodItemId, Integer quantity) {
        this.foodItemId = foodItemId;
        this.quantity = quantity;
    }

    public Long getFoodItemId() {
        return foodItemId;
    }

    public void setFoodItemId(Long foodItemId) {
        this.foodItemId = foodItemId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}