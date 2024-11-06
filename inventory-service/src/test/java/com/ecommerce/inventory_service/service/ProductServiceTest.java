package com.ecommerce.inventory_service.service;


import com.ecommerce.inventory_service.common.constant.constant;
import com.ecommerce.inventory_service.common.exception.MainException;
import com.ecommerce.inventory_service.entity.Product;
import com.ecommerce.inventory_service.model.ProductRequest;
import com.ecommerce.inventory_service.model.UpdateProductRequest;
import com.ecommerce.inventory_service.repository.ProductRepository;
import com.ecommerce.inventory_service.service.Impl.ProductServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ProductServiceTest {
    @InjectMocks
    private ProductServiceImpl productService;

    @Mock
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void createProduct_withNonExistProductName_shouldCreateProduct() {
        ProductRequest request = ProductRequest.builder()
                .productName("product test")
                .price(100000L)
                .quantity(10)
                .build();
        when(productRepository.findByProductName(request.getProductName())).thenReturn(Optional.empty());
        Product response = productService.createProduct(request, constant.SYSTEM);

        assertEquals(request.getProductName(), response.getProductName());
        assertEquals(request.getQuantity(), response.getQuantity());
        assertEquals(request.getPrice(), response.getPrice());
        assertEquals(constant.SYSTEM, response.getCreatedBy());
    }

    @Test
    void createProduct_withExistProductName_shouldReturnException() {
        ProductRequest request = ProductRequest.builder()
                .productName("product test")
                .price(100000L)
                .quantity(10)
                .build();
        when(productRepository.findByProductName(request.getProductName()))
                .thenReturn(Optional.of(Product.builder().productName("product test").build()));
        try {
            productService.createProduct(request, constant.SYSTEM);
        } catch (MainException expected) {
            assertEquals(constant.CODE_VALIDATION, expected.getCode());
            assertEquals("product dengan nama product test sudah pernah tersimpan", expected.getMessage());
        }
    }

    @Test
    void updateProductQuantity_withValidDeductionRequest_shouldUpdateQuantity() {
        UpdateProductRequest request = UpdateProductRequest.builder()
                .quantity(1)
                .updateType(constant.DEDUCTION)
                .build();
        long productId = 1;
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(Product.builder()
                        .id(1L)
                        .productName("test product")
                        .quantity(10)
                        .build()));
        Product response = productService.updateProductQuantity(productId, request, constant.SYSTEM);

        assertEquals(10 - 1, response.getQuantity());
    }

    @Test
    void updateProductQuantity_withQuantityRequestMoreThanActualQuantity_shouldReturnException() {
        UpdateProductRequest request = UpdateProductRequest.builder()
                .quantity(11)
                .updateType(constant.DEDUCTION)
                .build();
        long productId = 1;
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(Product.builder()
                        .id(1L)
                        .productName("test product")
                        .quantity(10)
                        .build()));
        try {
            productService.updateProductQuantity(productId, request, constant.SYSTEM);
        } catch (MainException expected) {
            assertEquals(constant.CODE_VALIDATION, expected.getCode());
            assertEquals("quantity akhir product tidak boleh minus", expected.getMessage());
        }
    }

    @Test
    void updateProductQuantity_withValidAdditionRequest_shouldUpdateQuantity() {
        UpdateProductRequest request = UpdateProductRequest.builder()
                .quantity(1)
                .updateType(constant.ADDITION)
                .build();
        long productId = 1;
        when(productRepository.findById(productId))
                .thenReturn(Optional.of(Product.builder()
                        .id(1L)
                        .productName("test product")
                        .quantity(10)
                        .build()));
        Product response = productService.updateProductQuantity(productId, request, constant.SYSTEM);

        assertEquals(10 + 1, response.getQuantity());
    }

    @Test
    void updateProductQuantity_withNotExistProductId_shouldReturnException() {
        UpdateProductRequest request = UpdateProductRequest.builder()
                .quantity(11)
                .updateType("ADDITION")
                .build();
        long productId = 1;
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());
        try {
            productService.updateProductQuantity(productId, request, "SYSTEM");
        } catch (MainException expected) {
            assertEquals("400-VALIDATION", expected.getCode());
            assertEquals("product tidak ditemukan", expected.getMessage());
        }
    }

    @Test
    void checkProductQuantity_withNotExistProductId_shouldReturnException() {
        long productId = 1;
        when(productRepository.findById(productId))
                .thenReturn(Optional.empty());
        try {
            productService.checkProductQuantity(productId);
        } catch (MainException expected) {
            assertEquals(constant.CODE_VALIDATION, expected.getCode());
            assertEquals("product tidak ditemukan", expected.getMessage());
        }
    }
}
