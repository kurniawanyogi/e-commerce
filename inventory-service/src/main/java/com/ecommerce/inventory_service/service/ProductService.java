package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.entity.Product;
import com.ecommerce.inventory_service.model.AuthPermission;
import com.ecommerce.inventory_service.model.ProductRequest;
import com.ecommerce.inventory_service.model.UpdateProductRequest;

public interface ProductService {
    Product createProduct(ProductRequest productRequest, String username, AuthPermission authPermission);
    Product updateProductQuantity(Long productId, UpdateProductRequest productRequest, String username, AuthPermission authPermission);
    Long checkProductQuantity(Long productId);
}