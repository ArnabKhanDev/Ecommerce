package com.ecommerce.service;

import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize);
    CategoryDTO createCategories(CategoryDTO categoryDTO);
    String deleteByCategoryId(Long categoryId);
    CategoryDTO updateByCategoryId(CategoryDTO category,Long categoryId);
}
