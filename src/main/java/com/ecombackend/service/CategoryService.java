package com.ecombackend.service;

import com.ecombackend.dto.CategoryDTO;
import com.ecombackend.dto.CategoryResponse;

import java.util.Optional;

public interface CategoryService {
    CategoryResponse geAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO CreateCategory(CategoryDTO categoryDTO);
    Optional<CategoryDTO> updateCategory(CategoryDTO categoryDTO, Long categoryId);
    CategoryDTO deleteCategory(Long id);
}