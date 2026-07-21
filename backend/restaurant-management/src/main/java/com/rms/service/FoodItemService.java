package com.rms.service;

import com.rms.dto.FoodItemDTO;
import com.rms.entity.FoodItem;
import com.rms.repository.FoodItemRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FoodItemService {

    @Autowired
    private FoodItemRepository foodItemRepository;

    @Autowired
    private ModelMapper modelMapper;


    public FoodItemDTO addFoodItem(FoodItemDTO dto) {

        FoodItem foodItem =
                modelMapper.map(dto, FoodItem.class);

        FoodItem savedFoodItem =
                foodItemRepository.save(foodItem);

        return modelMapper.map(savedFoodItem, FoodItemDTO.class);
    }

    public List<FoodItemDTO> getAllFoodItems() {

        List<FoodItem> foodItems =
                foodItemRepository.findAll();

        return foodItems.stream()
                .map(foodItem ->
                        modelMapper.map(foodItem, FoodItemDTO.class))
                .toList();
    }

    public FoodItemDTO getFoodItemById(Long id) {

        FoodItem foodItem =
                foodItemRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Food item not found"));

        return modelMapper.map(foodItem, FoodItemDTO.class);
    }

    public FoodItemDTO updateFoodItem(Long id, FoodItemDTO dto) {

        FoodItem existingFoodItem =
                foodItemRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Food item not found"));

        existingFoodItem.setName(dto.getName());
        existingFoodItem.setDescription(dto.getDescription());
        existingFoodItem.setPrice(dto.getPrice());
        existingFoodItem.setCategory(dto.getCategory());
        existingFoodItem.setAvailable(dto.isAvailable());

        FoodItem updatedFoodItem =
                foodItemRepository.save(existingFoodItem);

        return modelMapper.map(
                updatedFoodItem,
                FoodItemDTO.class
        );
    }public void deleteFoodItem(Long id) {

        FoodItem foodItem =
                foodItemRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Food item not found"));

        foodItemRepository.delete(foodItem);
    }


}