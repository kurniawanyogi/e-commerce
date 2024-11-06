package com.eccomerce.auth_service.service;


import com.eccomerce.auth_service.entity.User;
import com.eccomerce.auth_service.model.ChangePasswordRequest;
import com.eccomerce.auth_service.model.RegistrationRequest;
import com.eccomerce.auth_service.model.UpdateCurrentUserRequest;
import com.eccomerce.auth_service.model.UpdateUserRequest;
import org.springframework.data.domain.Page;

public interface UserService {
    User registrationUser(RegistrationRequest request, String username);

    User updateUser(UpdateUserRequest request, String userId, String username);

    User updateCurrentUser(UpdateCurrentUserRequest request, String userId, String username);

    User changePassword(ChangePasswordRequest request, String userId, String username);

    User getDetail(String id);

    Page<User> getList(Integer index, Integer size, String username, String firstName);
}
