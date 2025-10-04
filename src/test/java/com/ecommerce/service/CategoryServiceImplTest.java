package com.ecommerce.service;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.ecommerce.exception.APIException;
import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;
import com.ecommerce.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;

import java.util.*;
@ExtendWith(MockitoExtension.class )
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category category;
    private CategoryDTO categoryDTO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Books");

        categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(1L);
        categoryDTO.setCategoryName("Books");
    }

    @Test
    public void testGetAllCategories_Success() {
        List<Category> categoryList = List.of(category);
        Page<Category> categoryPage = new PageImpl<>(categoryList);
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        CategoryResponse response = categoryService.getAllCategories(0, 10);

        assertNotNull(response);
        assertEquals(1, response.getCategoryDTOList().size());
        assertEquals("Books", response.getCategoryDTOList().get(0).getCategoryName());
    }

    @Test
    public void testGetAllCategories_Empty() {
        Page<Category> categoryPage = new PageImpl<>(Collections.emptyList());
        when(categoryRepository.findAll(any(Pageable.class))).thenReturn(categoryPage);

        APIException ex = assertThrows(APIException.class, () -> {
            categoryService.getAllCategories(0, 10);
        });
        assertEquals("No Category created till now", ex.getMessage());
    }

    @Test
    public void testCreateCategories_Success() {
        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.findByCategoryName("Books")).thenReturn(null);
        when(categoryRepository.save(category)).thenReturn(category);
        when(modelMapper.map(category, CategoryDTO.class)).thenReturn(categoryDTO);

        CategoryDTO result = categoryService.createCategories(categoryDTO);

        assertNotNull(result);
        assertEquals("Books", result.getCategoryName());
    }

    @Test
    public void testCreateCategories_DuplicateCategory() {
        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(category);
        when(categoryRepository.findByCategoryName("Books")).thenReturn(category);

        APIException ex = assertThrows(APIException.class, () -> {
            categoryService.createCategories(categoryDTO);
        });
        assertTrue(ex.getMessage().contains("already present"));
    }

    @Test
    public void testDeleteByCategoryId_Success() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        String result = categoryService.deleteByCategoryId(1L);
        assertTrue(result.contains("Deleted"));
        verify(categoryRepository).delete(category);
    }

    @Test
    public void testDeleteByCategoryId_NotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteByCategoryId(2L);
        });
    }

    @Test
    public void testUpdateByCategoryId_Success() {
        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setCategoryName("Updated");
        CategoryDTO updatedDTO = new CategoryDTO();
        updatedDTO.setCategoryId(1L);
        updatedDTO.setCategoryName("Updated");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(modelMapper.map(categoryDTO, Category.class)).thenReturn(updatedCategory);
        when(categoryRepository.save(updatedCategory)).thenReturn(updatedCategory);
        when(modelMapper.map(updatedCategory, CategoryDTO.class)).thenReturn(updatedDTO);

        CategoryDTO result = categoryService.updateByCategoryId(categoryDTO, 1L);
        assertEquals("Updated", result.getCategoryName());
    }

    @Test
    public void testUpdateByCategoryId_NotFound() {
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateByCategoryId(categoryDTO, 2L);
        });
    }
}
