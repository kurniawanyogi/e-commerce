package com.eccomerce.auth_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.eccomerce.auth_service.validator.FieldMatcher;
import com.eccomerce.auth_service.validator.Password;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@FieldMatcher(firstField = "password", secondField = "confirmPassword", message = "wrong confirm password")
public class RegistrationRequest {
    @NotEmpty
    @Size(max = 50)
    private String firstName;

    @Size(max = 50)
    @NotEmpty
    private String lastName;

    @NotEmpty
    @Size(min = 5, max = 30)
    private String username;

    @NotNull
    private Integer roleId;

    private Character status;

    @Password
    @NotEmpty
    @Size(min = 8, max = 20)
    private String password;
    @Size(min = 8, max = 20)
    private String confirmPassword;
}
