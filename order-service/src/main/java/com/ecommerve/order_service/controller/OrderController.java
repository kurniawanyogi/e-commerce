package com.ecommerve.order_service.controller;

import com.ecommerve.order_service.common.model.BaseResponse;
import com.ecommerve.order_service.model.OrderRequest;
import com.ecommerve.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping(path = "/{productId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> createProduct(@Valid @RequestBody OrderRequest orderRequest, @PathVariable long productId) {
        orderService.orderProduct(productId, orderRequest, "SYSTEM");
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "Success Order Product", null));
    }
}
