package com.ecommerce.service;

import com.ecommerce.exception.APIException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;
import com.ecommerce.repository.CategoryRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Category> categoryList = categoryRepository.findAll(pageable);
        List<Category> categories = categoryList.getContent();
        if(categories.isEmpty())
            throw new APIException("No Category created till now");
        List<CategoryDTO> categoryDTOS = categoryList.stream()
                .map(category -> modelMapper.map(category,CategoryDTO.class))
                .toList();

        CategoryResponse categoryResponse = new CategoryResponse();
        categoryResponse.setCategoryDTOList(categoryDTOS);
        return categoryResponse;
    }

    @Override
    public CategoryDTO createCategories(CategoryDTO categoryDTO) {
        Category category = modelMapper.map(categoryDTO,Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDB != null)
        {
            throw new APIException("Category " + category.getCategoryName() + " is already present");
        }
        Category savedCategory =  categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @SneakyThrows
    @Override
    public String deleteByCategoryId(Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category" , "CategoryId", categoryId));
        categoryRepository.delete(savedCategory);
        return categoryId + "  Category Id Deleted";
    }

    @SneakyThrows
    @Override
    public CategoryDTO updateByCategoryId(CategoryDTO categoryDTO,Long categoryId) {
        Category savedCategory = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category" , "CategoryId", categoryId));

        Category category = modelMapper.map(categoryDTO,Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory,CategoryDTO.class);
    }
}
