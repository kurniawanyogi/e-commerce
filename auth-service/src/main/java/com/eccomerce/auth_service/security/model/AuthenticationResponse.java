package com.eccomerce.auth_service.security.model;

import com.eccomerce.auth_service.entity.Role;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuthenticationResponse {
    private String token;
    private String userId;
    private Set<Role> roles;
}
