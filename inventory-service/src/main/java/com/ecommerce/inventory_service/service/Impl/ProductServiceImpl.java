package com.ecommerce.inventory_service.service.Impl;

import com.ecommerce.inventory_service.client.auth.AuthClient;
import com.ecommerce.inventory_service.common.constant.Constant;
import com.ecommerce.inventory_service.common.exception.MainException;
import com.ecommerce.inventory_service.common.model.BaseResponse;
import com.ecommerce.inventory_service.entity.Product;
import com.ecommerce.inventory_service.model.AuthPermission;
import com.ecommerce.inventory_service.model.ProductRequest;
import com.ecommerce.inventory_service.model.UpdateProductRequest;
import com.ecommerce.inventory_service.repository.ProductRepository;
import com.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final AuthClient authClient;

    @Override
    @Transactional
    public Product createProduct(ProductRequest productRequest, String username, AuthPermission authPermission) {
        authorizePermission(authPermission);

        Optional<Product> existingProduct = productRepository.findByProductName(productRequest.getProductName());
        if (existingProduct.isPresent())
            throw new MainException(Constant.CODE_VALIDATION,
                    String.format("product dengan nama %s sudah pernah tersimpan", productRequest.getProductName()));

        return saveProduct(productRequest, username);
    }

    @Override
    @Transactional
    public Product updateProductQuantity(Long productId, UpdateProductRequest productRequest, String username, AuthPermission authPermission) {
        authorizePermission(authPermission);
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (!existingProduct.isPresent())
            throw new MainException(Constant.CODE_VALIDATION, "product tidak ditemukan");

        return updateProduct(existingProduct.get(), productRequest, username);
    }

    @Override
    public Long checkProductQuantity(Long productId) {
        Optional<Product> existingProduct = productRepository.findById(productId);
        if (!existingProduct.isPresent())
            throw new MainException(Constant.CODE_VALIDATION, "product tidak ditemukan");
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
        //update quantity based from updateType
        if (Constant.DEDUCTION.equals(request.getUpdateType())) {
            quantity = existingProduct.getQuantity() - request.getQuantity();
            if (quantity < 0)
                throw new MainException(Constant.CODE_VALIDATION, "quantity akhir product tidak boleh minus");
        } else if (Constant.ADDITION.equals(request.getUpdateType())) {
            quantity = existingProduct.getQuantity() + request.getQuantity();
        } else {
            throw new MainException(Constant.CODE_VALIDATION, "updateType tidak valid");
        }
        existingProduct.setQuantity(quantity);
        existingProduct.setLastUpdatedBy(username);
        existingProduct.setLastUpdatedDate(new Timestamp(new Date().getTime()));

        productRepository.save(existingProduct);
        return existingProduct;
    }

    private void authorizePermission(AuthPermission authPermission) {
        Mono<BaseResponse> quantityResponse = authClient.authorize(authPermission.getToken(), authPermission.getPermissionRole());
        BaseResponse response = quantityResponse.block();

        if (response == null) {
            throw new MainException("500-AUTH-SERVICE", "internal server error");
        }

        if (!response.getCode().equals(HttpStatus.OK.toString())) {
            throw new MainException(response.getCode(), response.getDescription());
        }
    }
}
