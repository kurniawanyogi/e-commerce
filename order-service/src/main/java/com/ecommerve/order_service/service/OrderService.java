package com.ecommerve.order_service.service;

import com.ecommerve.order_service.model.OrderRequest;

public interface OrderService {
    void orderProduct(long productId, OrderRequest orderRequest, String username);
}
