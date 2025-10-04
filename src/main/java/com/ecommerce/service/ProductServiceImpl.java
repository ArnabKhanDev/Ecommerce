package com.ecommerce.service;

import com.ecommerce.exception.ResourceNotFoundException;
import com.ecommerce.model.Category;
import com.ecommerce.model.Product;
import com.ecommerce.payload.ProductDTO;
import com.ecommerce.payload.ProductResponse;
import com.ecommerce.repository.CategoryRepository;
import com.ecommerce.repository.ProductRepository;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;


    @SneakyThrows
    @Override
    public ProductDTO addProduct(ProductDTO productDTO, Long categoryId) {
        Product product = modelMapper.map(productDTO, Product.class);
        Category category = categoryRepository.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category", "CategoryId", categoryId));

        product.setCategory(category);
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts() {
        List<Product> productList = productRepository.findAll();
        List<ProductDTO> productDTOS = productList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductDTOS(productDTOS);
        return productResponse;
    }

    @SneakyThrows
    @Override
    public ProductResponse findProductsByCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "CategoryId", categoryId));
        List<Product> products = productRepository.findByCategory(category);
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductDTOS(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {
        List<Product> products = productRepository.findByProductName(keyword);
        List<ProductDTO> productDTOS = products.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductDTOS(productDTOS);
        return productResponse;
    }

    @SneakyThrows
    @Override
    public ProductDTO updateProduct(ProductDTO product, Long productId) {
       Product productFromDB = productRepository.findById(productId)
               .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));
       productFromDB.setSpecialPrice(100);
       Product savedProduct = productRepository.save(productFromDB);
       return modelMapper.map(savedProduct,ProductDTO.class);
    }

    @SneakyThrows
    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product productFromDB = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));
        productRepository.delete(productFromDB);
        return modelMapper.map(productFromDB,ProductDTO.class);
    }
}
