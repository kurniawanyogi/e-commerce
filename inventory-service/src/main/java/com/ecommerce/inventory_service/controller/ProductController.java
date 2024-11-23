package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.common.exception.MainException;
import com.ecommerce.inventory_service.common.model.BaseResponse;
import com.ecommerce.inventory_service.entity.Product;
import com.ecommerce.inventory_service.model.AuthPermission;
import com.ecommerce.inventory_service.model.ProductRequest;
import com.ecommerce.inventory_service.model.UpdateProductRequest;
import com.ecommerce.inventory_service.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ecommerce.inventory_service.common.constant.Constant.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> createProduct(@Valid @RequestBody ProductRequest productRequest, HttpServletRequest request) {
        String token = getTokenHeader(request);
        Product response = productService.createProduct(
                productRequest,
                SYSTEM,
                AuthPermission.builder()
                .token(token)
                .permissionRole(ROLE_ADMIN)
                .build());
        return ResponseEntity.ok(new BaseResponse(HttpStatus.CREATED.toString(), "Create Product Success", response));
    }

    @GetMapping(path = "/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> getProductQuantity(@PathVariable long productId) {
        long response = productService.checkProductQuantity(productId);

        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "Get Product Quantity Success", response));
    }

    @PutMapping(path = "/{productId}/deduction", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> deductionProductQuantity(@Valid @RequestBody UpdateProductRequest productRequest, @PathVariable long productId, HttpServletRequest request) {
        String token = getTokenHeader(request);

        //set updateType to DEDUCTION
        productRequest.setUpdateType(DEDUCTION);
        Product response = productService.updateProductQuantity(
                productId,
                productRequest,
                SYSTEM,
                AuthPermission.builder()
                        .token(token)
                        .permissionRole(ROLE_ADMIN)
                        .build());
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "Update Product Success", response));
    }

    @PutMapping(path = "/{productId}/addition", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> additionProductQuantity(@Valid @RequestBody UpdateProductRequest productRequest, @PathVariable long productId, HttpServletRequest request) {
        String token = getTokenHeader(request);

        //set updateType to ADDITION
        productRequest.setUpdateType(ADDITION);
        Product response = productService.updateProductQuantity(
                productId,
                productRequest,
                SYSTEM,
                AuthPermission.builder()
                        .token(token)
                        .permissionRole(ROLE_ADMIN)
                        .build());
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "Update Product Success", response));
    }

    private String getTokenHeader(HttpServletRequest request) {
        String token = request.getHeader(TOKEN);
        if (token == null) {
            throw new MainException(HttpStatus.UNAUTHORIZED.toString(), "TOKEN required");
        }
        return token;
    }
}
