package com.ecombackend.service.impl;

import com.ecombackend.dto.CategoryDTO;
import com.ecombackend.dto.CategoryResponse;
import com.ecombackend.entity.Category;
import com.ecombackend.exception.APIException;
import com.ecombackend.exception.ResourceNotFoundException;
import com.ecombackend.repository.CategoryRepository;
import com.ecombackend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public CategoryResponse geAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Sort sortByOrder = sortOrder.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending():Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(pageNumber, pageSize,sortByOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);
        if (categoryPage.isEmpty()) {
            throw new APIException("No Category found!!");
        }
        List<CategoryDTO> categoryDTOS = categoryPage.getContent().stream()
                .map(category -> modelMapper.map(category, CategoryDTO.class))
                .toList();
        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategories(categoryDTOS);
        categoryResponse.setPageNumber(categoryPage.getNumber());
        categoryResponse.setPageSize(categoryPage.getSize());
        categoryResponse.setTotalElements(categoryPage.getTotalElements());
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        return categoryResponse;
    }
    @Override
    public CategoryDTO CreateCategory(CategoryDTO categoryDTO) {
        Category savedCategory = categoryRepository.findByCategoryName(categoryDTO.getCategoryName());
        if (savedCategory != null) {
            throw new APIException("Category already exists!!");
        }
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category newCategory = categoryRepository.save(category);

        return modelMapper.map(newCategory, CategoryDTO.class);
    }
    @Override
    public Optional<CategoryDTO> updateCategory(CategoryDTO categoryDTO, Long categoryId) {
        Optional<Category> existingCategoryOpt = categoryRepository.findById(categoryId);
        if (existingCategoryOpt.isEmpty()) {
            return Optional.empty();
        }
        Category existingCategory = existingCategoryOpt.get();
        existingCategory.setCategoryName(categoryDTO.getCategoryName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return Optional.of(modelMapper.map(updatedCategory, CategoryDTO.class));

    }
    @Override
    public CategoryDTO deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "id", id));
        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

}