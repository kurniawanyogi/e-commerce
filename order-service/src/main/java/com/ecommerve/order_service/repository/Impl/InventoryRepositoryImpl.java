package com.ecommerve.order_service.repository.Impl;

import com.ecommerve.order_service.common.exception.MainException;
import com.ecommerve.order_service.common.model.BaseResponse;
import com.ecommerve.order_service.model.OrderRequest;
import com.ecommerve.order_service.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Repository
public class InventoryRepositoryImpl implements InventoryRepository {
    private final WebClient webClient;

    @Autowired
    public InventoryRepositoryImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public Mono<BaseResponse> getProductQuantityByProductId(long productId) {
        return webClient.get()
                .uri(String.format("http://localhost:8080/products/%d",productId))
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(BaseResponse.class)
                                .flatMap(error -> Mono.error(new MainException("500", "inventory-service response: " + error.getDescription()))))
                .bodyToMono(BaseResponse.class);
    }

    public Mono<BaseResponse> deductionProductQuantity(long productId, OrderRequest request) {
        return webClient.put()
                .uri(String.format("http://localhost:8080/products/%d/deduction", productId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(request), OrderRequest.class)
                .retrieve()
                .bodyToMono(BaseResponse.class);
    }

    private Mono<? extends Throwable> handleErrorResponse(HttpStatus statusCode) {

        return Mono.error(new MainException("500", "Failed to fetch inventory. Status code: " + statusCode));
    }
}