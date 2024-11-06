package com.eccomerce.auth_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UpdateUserRequest extends UpdateCurrentUserRequest {
    @Size(max = 3)
    private String groupId;

    private Integer roleId;

    private Character status;

}
