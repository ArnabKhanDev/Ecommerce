package com.ecommerce.controller;

import com.ecommerce.payload.CategoryDTO;
import com.ecommerce.payload.CategoryResponse;
import com.ecommerce.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Tag(name = "Category APIs" , description = "APIs for managing category")
    @Operation(summary = "Get Categories", description = "API to retrieve categories")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Retrieve categories successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            @ApiResponse(responseCode = "404", description = "Categories Not Found")
    })
    @GetMapping("/public/categories")
    public ResponseEntity<CategoryResponse> getCategories(
            @RequestParam Integer pageNumber,
            @RequestParam Integer pageSize) {
        CategoryResponse categoryResponse = categoryService.getAllCategories(pageNumber,pageSize);
        return new ResponseEntity<>(categoryResponse, HttpStatus.OK);
    }

    @Tag(name = "Category APIs" , description = "APIs for managing category")
    @PostMapping("/public/categories")
    public ResponseEntity<String> addCategories(@Valid @RequestBody CategoryDTO category) {
        categoryService.createCategories(category);
        return new ResponseEntity<>("Category Created", HttpStatus.CREATED);
    }

    @Tag(name = "Category APIs" , description = "APIs for managing category")
    @DeleteMapping("/admin/categories/{categoryId}")
    public ResponseEntity<String> deleteCategory(@Parameter(description = "Id of the category that you want to delete") @PathVariable Long categoryId) {
        String status = categoryService.deleteByCategoryId(categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @Tag(name = "Category APIs" , description = "APIs for managing category")
    @PutMapping("/public/categories/{categoryId}")
    public ResponseEntity<?> updateCategory(@RequestBody CategoryDTO category, @PathVariable Long categoryId) {
        CategoryDTO status = categoryService.updateByCategoryId(category, categoryId);
        return new ResponseEntity<>(status, HttpStatus.OK);

    }


}
