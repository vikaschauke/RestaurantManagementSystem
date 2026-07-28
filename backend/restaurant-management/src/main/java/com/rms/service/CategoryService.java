package com.rms.service;

import com.rms.dto.CategoryDTO;
import com.rms.entity.Category;
import com.rms.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    public CategoryDTO addCategory(CategoryDTO categoryDTO) {

        Category category =
                modelMapper.map(categoryDTO, Category.class);

        Category savedCategory =
                categoryRepository.save(category);

        return modelMapper.map(
                savedCategory,
                CategoryDTO.class
        );
    }

    public List<CategoryDTO> getAllCategories() {

        return categoryRepository.findAll()
                .stream()
                .map(category ->
                        modelMapper.map(category, CategoryDTO.class))
                .toList();
    }

    public CategoryDTO getCategoryById(Long id) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new NoSuchElementException("Category not found"));

        return modelMapper.map(
                category,
                CategoryDTO.class
        );

    }

    public CategoryDTO updateCategory(
            Long id,
            CategoryDTO categoryDTO) {

        Category category =
                categoryRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Category not found"));

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        Category updatedCategory =
                categoryRepository.save(category);

        return modelMapper.map(
                updatedCategory,
                CategoryDTO.class
        );
    }

    public String deleteCategory(Long id) {

        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found");
        }

        categoryRepository.deleteById(id);

        return "Category deleted successfully";
    }
}