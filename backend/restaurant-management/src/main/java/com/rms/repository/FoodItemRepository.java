package com.rms.repository;

import com.rms.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {


    List<FoodItem> findByNameContainingIgnoreCase(String name);

}

