package com.eccomerce.auth_service.security.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class LoginRequest {
    private String username;
    private String password;
}
