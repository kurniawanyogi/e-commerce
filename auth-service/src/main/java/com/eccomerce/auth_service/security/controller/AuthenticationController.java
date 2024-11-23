package com.eccomerce.auth_service.security.controller;

import com.eccomerce.auth_service.common.constant.Constant;
import com.eccomerce.auth_service.entity.User;
import com.eccomerce.auth_service.exception.MainException;
import com.eccomerce.auth_service.model.ChangePasswordRequest;
import com.eccomerce.auth_service.model.RegistrationRequest;
import com.eccomerce.auth_service.model.UpdateCurrentUserRequest;
import com.eccomerce.auth_service.model.UpdateUserRequest;
import com.eccomerce.auth_service.response.BaseResponse;
import com.eccomerce.auth_service.security.entity.Authentication;
import com.eccomerce.auth_service.security.model.AuthenticationResponse;
import com.eccomerce.auth_service.security.model.LoginRequest;
import com.eccomerce.auth_service.security.service.UserDetailServiceImpl;
import com.eccomerce.auth_service.security.util.TokenUtil;
import com.eccomerce.auth_service.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/user")
public class AuthenticationController {
    private final AuthenticationManager authenticationManager;
    private final UserDetailServiceImpl userDetailsService;
    private final TokenUtil tokenUtil;
    private final UserService userService;


    public Authentication getCurrentUser() {
        return (Authentication) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping(path = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (Exception exception) {
            throw new MainException(HttpStatus.UNAUTHORIZED.toString(), exception.getMessage());
        }
        Authentication authentication = (Authentication) userDetailsService.loadUserByUsername(request.getUsername());
        String token = tokenUtil.generateToken(authentication);
        AuthenticationResponse response = new AuthenticationResponse(token, authentication.getUserId(), authentication.getUser().getRoles());
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "login success", response));
    }

    @GetMapping(path = "/authorize", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> authorize(HttpServletRequest request) {
        String permissionRole = request.getHeader(Constant.PERMISSION);
        if (permissionRole == null) {
            throw new MainException(HttpStatus.UNAUTHORIZED.toString(), "Permission required");
        }
        SimpleGrantedAuthority role = new SimpleGrantedAuthority("ROLE_" + permissionRole);
        if (!getCurrentUser().getAuthorities().contains(role)) {
            throw new MainException(HttpStatus.FORBIDDEN.toString(), "FORBIDDEN ACCESS");
        }

        User response = userService.getDetail(getCurrentUser().getUserId());
        return ResponseEntity.ok(new BaseResponse(HttpStatus.OK.toString(), "Detail User", response));
    }

    //TODO api registration
    //with @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(path = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> registration(@Valid @RequestBody RegistrationRequest request) {
        User response = userService.registrationUser(request, "ADMIN");

        return ResponseEntity.ok(new BaseResponse(HttpStatus.CREATED.toString(), "Registration User Success", response));
    }
}
