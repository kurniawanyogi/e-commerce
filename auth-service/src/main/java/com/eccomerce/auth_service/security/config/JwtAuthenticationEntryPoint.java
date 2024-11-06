package com.eccomerce.auth_service.security.config;

import com.eccomerce.auth_service.response.BaseResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(String.valueOf(HttpStatus.UNAUTHORIZED));
        baseResponse.setDescription("UnAuthorized Access");
        baseResponse.setAdditionalEntity(null);

        byte[] responseToSend = new ObjectMapper().writeValueAsString(baseResponse).getBytes();
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().write(responseToSend);
    }
}
