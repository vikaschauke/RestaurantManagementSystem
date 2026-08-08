package com.rms.repository;

import com.rms.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {


    List<FoodItem> findByNameContainingIgnoreCase(String name);

    List<FoodItem> findByCategoryIgnoreCase(String category);

    List<FoodItem> findByPriceBetween(double minPrice, double maxPrice);

    List<FoodItem> findByAvailable(boolean available);

    List<FoodItem> findByCategoryIgnoreCaseAndPriceBetweenAndAvailable(
            String category,
            double minPrice,
            double maxPrice,
            boolean available
    );

}

