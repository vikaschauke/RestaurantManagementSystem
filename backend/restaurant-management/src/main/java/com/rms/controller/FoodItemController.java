package com.rms.controller;

import com.rms.dto.FoodItemDTO;
import com.rms.service.FoodItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/food-items")
public class FoodItemController {

    @Autowired
    private FoodItemService foodItemService;

    @PostMapping
    public ResponseEntity<FoodItemDTO> addFoodItem(
            @RequestBody FoodItemDTO dto) {

        FoodItemDTO savedFoodItem =
                foodItemService.addFoodItem(dto);

        return ResponseEntity.ok(savedFoodItem);
    }

    @GetMapping
    public ResponseEntity<List<FoodItemDTO>> getAllFoodItems() {

        return ResponseEntity.ok(
                foodItemService.getAllFoodItems()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FoodItemDTO> getFoodItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                foodItemService.getFoodItemById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<FoodItemDTO> updateFoodItem(
            @PathVariable Long id,
            @RequestBody FoodItemDTO dto) {

        return ResponseEntity.ok(
                foodItemService.updateFoodItem(id, dto)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFoodItem(
            @PathVariable Long id) {

        foodItemService.deleteFoodItem(id);

        return ResponseEntity.ok("Food item deleted successfully");
    }
}