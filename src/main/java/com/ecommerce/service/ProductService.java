package com.ecommerce.service;

import com.ecommerce.payload.ProductDTO;
import com.ecommerce.payload.ProductResponse;

public interface ProductService {
    ProductDTO addProduct(ProductDTO product, Long categoryId);
    ProductResponse getAllProducts();
    ProductResponse findProductsByCategory(Long categoryId);
    ProductResponse searchProductByKeyword(String keyword);
    ProductDTO updateProduct(ProductDTO product, Long productId);
    ProductDTO deleteProduct(Long productId);
}
