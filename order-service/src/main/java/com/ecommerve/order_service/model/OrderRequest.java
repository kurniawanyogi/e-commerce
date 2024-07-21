package com.ecommerve.order_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderRequest {
    @Min(0)
    private long quantity;
}
