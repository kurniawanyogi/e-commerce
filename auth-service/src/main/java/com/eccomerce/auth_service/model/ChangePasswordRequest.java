package com.eccomerce.auth_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.eccomerce.auth_service.validator.FieldMatcher;
import com.eccomerce.auth_service.validator.Password;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldMatcher(firstField = "newPassword", secondField = "confirmPassword", message = "wrong confirm password")
public class ChangePasswordRequest {
    @NotEmpty
    private String existingPassword;
    @NotEmpty
    @Password
    @Size(min = 8, max = 20)
    private String newPassword;
    @NotEmpty
    private String confirmPassword;
}
