package com.ecommerve.order_service.service.Impl;

import com.ecommerve.order_service.common.exception.MainException;
import com.ecommerve.order_service.common.kafka.KafkaProducer;
import com.ecommerve.order_service.common.model.BaseResponse;
import com.ecommerve.order_service.model.OrderRequest;
import com.ecommerve.order_service.repository.InventoryRepository;
import com.ecommerve.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final InventoryRepository inventoryRepository;
    private final KafkaProducer kafkaProducer;

    @Override
    public void orderProduct(long productId, OrderRequest orderRequest, String username) {

        Mono<BaseResponse> quantityResponse = inventoryRepository.getProductQuantityByProductId(productId);
        BaseResponse response = quantityResponse.block();

        // if success check quantity product then compare actual quantity product with quantity order
        if (response != null && response.getCode().equals(HttpStatus.OK.toString())) {
            if ((Integer) response.getAdditionalEntity().get("result") >= orderRequest.getQuantity()) {
                // call endpoint deduction/update quantity product
                Mono<BaseResponse> deductionResponse = inventoryRepository.deductionProductQuantity(productId, orderRequest);
                BaseResponse responseDeduction = deductionResponse.block();
                if (responseDeduction != null && !responseDeduction.getCode().equals(HttpStatus.OK.toString())) {
                    throw new MainException("400-VALIDATION", responseDeduction.getDescription());
                }
                //TODO set value, message sent to kafka
                kafkaProducer.sendMessage("orders.order.completed", orderRequest.toString());
            } else {
                throw new MainException("400-VALIDATION", "quantity product kurang dari quantity order");
            }
        } else {
            assert response != null;
            throw new MainException("500", response.getDescription());
        }
    }
}
