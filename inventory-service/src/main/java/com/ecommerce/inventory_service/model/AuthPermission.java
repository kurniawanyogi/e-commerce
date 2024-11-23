package com.ecommerce.inventory_service.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@ToString
public class AuthPermission {
    private String permissionRole;
    private String token;
}
