package com.ecommerce.inventory_service.service.Impl;

import com.ecommerce.inventory_service.common.exception.MainException;
import com.ecommerce.inventory_service.entity.Product;
import com.ecommerce.inventory_service.model.ProductRequest;
import com.ecommerce.inventory_service.model.UpdateProductRequest;
import com.ecommerce.inventory_service.repository.ProductRepository;
import com.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Product createProduct(ProductRequest productRequest, String username) {
        Optional<Product> existingProduct = productRepository.findByProductName(productRequest.getProductName());
        if (existingProduct.isPresent())
            throw new MainException("400-VALIDATION",
                    String.format("product dengan nama %s sudah pernah tersimpan", productRequest.getProductName()));

        return saveProduct(productRequest, username);
    }

    @Override
    @Transactional
    public Product updateProductQuantity(Long productId, UpdateProductRequest productRequest, String username) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (!existingProduct.isPresent())
            throw new MainException("400-VALIDATION", "product tidak ditemukan");

        return updateProduct(existingProduct.get(), productRequest, username);
    }

    @Override
    public Long checkProductQuantity(Long productId) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (!existingProduct.isPresent())
            throw new MainException("400-VALIDATION", "product tidak ditemukan");
        return existingProduct.get().getQuantity();
    }

    private Product saveProduct(ProductRequest productRequest, String username) {
        Product product = new Product();
        product.setProductName(productRequest.getProductName());
        product.setQuantity(productRequest.getQuantity());
        product.setPrice(productRequest.getPrice());

        product.setCreatedBy(username);
        product.setCreatedDate(new Timestamp(new Date().getTime()));
        product.setLastUpdatedBy(username);
        product.setLastUpdatedDate(new Timestamp(new Date().getTime()));
        productRepository.save(product);
        return product;
    }

    private Product updateProduct(Product existingProduct, UpdateProductRequest request, String username) {
        long quantity;
        if ("DEDUCTION".equals(request.getUpdateType())) {
            quantity = existingProduct.getQuantity() - request.getQuantity();
            if (quantity < 0)
                throw new MainException("400-VALIDATION", "quantity akhir product tidak boleh minus");
        } else if ("ADDITION".equals(request.getUpdateType())) {
            quantity = existingProduct.getQuantity() + request.getQuantity();
        } else {
            throw new MainException("400-VALIDATION", "updateType tidak valid");
        }
        existingProduct.setQuantity(quantity);
        existingProduct.setLastUpdatedBy(username);
        existingProduct.setLastUpdatedDate(new Timestamp(new Date().getTime()));

        productRepository.save(existingProduct);
        return existingProduct;
    }
}
