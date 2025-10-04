package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.payload.ProductDTO;
import com.ecommerce.payload.ProductResponse;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {
    @Mock
    CategoryRepository categoryRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    ProductServiceImpl productService;

    Product product;
    ProductDTO productDTO;
    Category category;


    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Books");

        product = new Product();
        product.setId(1L);
        product.setProductName("Story");
        product.setPrice(100.0);
        product.setQuantity(2);
        product.setProductDescription("Comedy Story");
        product.setSpecialPrice(50.0);
        product.setCategory(category);

        productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setProductName("Story");
        productDTO.setPrice(100.0);
        productDTO.setQuantity(2);
        productDTO.setProductDescription("Comedy Story");

    }

    @Test
    void testAddProduct_Success()
    {
        when(modelMapper.map(productDTO,Product.class)).thenReturn(product);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(productRepository.save(product)).thenReturn(product);
        when(modelMapper.map(product,ProductDTO.class)).thenReturn(productDTO);
        ProductDTO result = productService.addProduct(productDTO,1L);
        assertNotNull(result);
        assertEquals("Story",result.getProductName());
    }

    @Test
    void testAddProduct_CategoryNotFound(){
        when(modelMapper.map(productDTO,Product.class)).thenReturn(product);
        when(categoryRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> productService.addProduct(productDTO,2L));

    }

    @Test
    void testGetAllProducts_Success(){
        List<Product> products = List.of(product);
        when(productRepository.findAll()).thenReturn(products);
        when(modelMapper.map(product,ProductDTO.class)).thenReturn(productDTO);
        ProductResponse productResponse = productService.getAllProducts();
        assertNotNull(productResponse);
        assertEquals("Story",productResponse.getProductDTOS().get(0).getProductName());
    }

    @Test
    void testFindProductsByCategory_Success(){
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        List<Product> products = List.of(product);
        when(productRepository.findByCategory(category)).thenReturn(products);
        when(modelMapper.map(product,ProductDTO.class)).thenReturn(productDTO);
        ProductResponse productResponse = productService.findProductsByCategory(category.getCategoryId());
        assertNotNull(productResponse);
        assertEquals("Story",productResponse.getProductDTOS().get(0).getProductName());
    }

    @Test
    void testSearchProductByKeyword_Success(){
        List<Product> products = List.of(product);
        when(productRepository.findByProductName("Story")).thenReturn(products);
        when(modelMapper.map(product,ProductDTO.class)).thenReturn(productDTO);
        ProductResponse productResponse = productService.searchProductByKeyword("Story");
        assertNotNull(productResponse);
        assertEquals("Story",productResponse.getProductDTOS().get(0).getProductName());
    }

    @Test
    void testUpdateProduct_Success()
    {
        ProductDTO updatedDTO = new ProductDTO();
        updatedDTO.setId(1L);
        updatedDTO.setProductName("Phone");
        updatedDTO.setPrice(100);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productRepository.save(product)).thenReturn(product);
        when(modelMapper.map(product,ProductDTO.class)).thenReturn(updatedDTO);
        ProductDTO result = productService.updateProduct(updatedDTO,1L);
        assertEquals(100,result.getPrice());
    }

    @Test
    void testDeleteProduct_Success(){
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(modelMapper.map(product,ProductDTO.class)).thenReturn(productDTO);
        ProductDTO result = productService.deleteProduct(1L);
        assertEquals("Story",result.getProductName());

    }

}