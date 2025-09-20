package com.ecommerce.service;

import com.ecommerce.model.Category;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void createCategories(Category category);
    String deleteByCategoryId(Long categoryId);
    Category updateByCategoryId(Category category,Long categoryId);
}
