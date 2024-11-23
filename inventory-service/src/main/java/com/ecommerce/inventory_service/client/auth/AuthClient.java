package com.ecommerce.inventory_service.client.auth;

import com.ecommerce.inventory_service.common.model.BaseResponse;
import reactor.core.publisher.Mono;

public interface AuthClient {
    Mono<BaseResponse> authorize(String token, String permissionRole);
}
