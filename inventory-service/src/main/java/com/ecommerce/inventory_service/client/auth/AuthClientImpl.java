package com.ecommerce.inventory_service.client.auth;

import com.ecommerce.inventory_service.common.constant.Constant;
import com.ecommerce.inventory_service.common.exception.MainException;
import com.ecommerce.inventory_service.common.model.BaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClientImpl implements AuthClient {
    private final WebClient webClient;
    @Value("${external-service.auth-service-url:http://localhost:8889/auth}")
    private String authServiceUrl;

    public AuthClientImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Mono<BaseResponse> authorize(String token, String permissionRole) {
        return webClient.get()
                .uri(authServiceUrl + "/user/authorize")
                .header(Constant.TOKEN, token)
                .header(Constant.PERMISSION, permissionRole)
                .retrieve()
                .onStatus(httpStatus -> !httpStatus.is2xxSuccessful(),
                        clientResponse -> clientResponse.bodyToMono(BaseResponse.class)
                                .flatMap(error -> Mono.error(new MainException(error.getCode(), "auth-service response: " + error.getDescription()))))
                .bodyToMono(BaseResponse.class);
    }
}
