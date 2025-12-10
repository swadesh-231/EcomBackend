package com.ecombackend.service;

import com.ecombackend.dto.CategoryDTO;
import com.ecombackend.dto.CategoryResponse;

import java.util.Optional;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO deleteCategory(Long categoryId);

    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}