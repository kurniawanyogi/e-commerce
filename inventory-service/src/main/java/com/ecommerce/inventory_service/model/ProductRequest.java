package com.ecommerce.inventory_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductRequest {
    @NotEmpty
    @Size(max = 150)
    private String productName;
    @Min(0)
    private Long price;
    @Min(0)
    private long quantity;
}
