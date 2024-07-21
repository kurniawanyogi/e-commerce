package com.ecommerve.order_service.repository;

import com.ecommerve.order_service.common.model.BaseResponse;
import com.ecommerve.order_service.model.OrderRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InventoryRepository {
    Mono<BaseResponse> getProductQuantityByProductId(long productId);

    Mono<BaseResponse> deductionProductQuantity(long productId, OrderRequest request);
}
