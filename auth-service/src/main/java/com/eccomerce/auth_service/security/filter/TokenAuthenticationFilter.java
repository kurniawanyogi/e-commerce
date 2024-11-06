package com.eccomerce.auth_service.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.eccomerce.auth_service.response.BaseResponse;
import com.eccomerce.auth_service.security.entity.Authentication;
import com.eccomerce.auth_service.security.service.UserDetailServiceImpl;
import com.eccomerce.auth_service.security.util.TokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class TokenAuthenticationFilter extends OncePerRequestFilter {
    private static final String TOKEN = "TOKEN";

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Autowired
    private TokenUtil tokenUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = request.getHeader(TOKEN);

        if (token != null) {
            String username = tokenUtil.getUsernameFromToken(token);
            if (username == null) {
                log.error("auth failed");
                unAuthorizeResponse(response);
                return;
            }
            Authentication userDetails = (Authentication) userDetailService.loadUserByUsername(username);
            if (tokenUtil.isValidToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = tokenUtil.getAuthentication(token, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.error("auth failed");
                unAuthorizeResponse(response);
                return;
            }
        }

        filterChain.doFilter(request, response);
        response.getOutputStream().close();
    }

    private void unAuthorizeResponse(HttpServletResponse response) throws IOException {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED));
        baseResponse.setDescription("unAuthorization Access");
        baseResponse.setAdditionalEntity(null);

        byte[] responseToSend = restResponseByte(baseResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getOutputStream().write(responseToSend);
    }

    private void forbiddenResponse(HttpServletResponse response) throws IOException {
        BaseResponse baseResponse = new BaseResponse();
        baseResponse.setCode(String.valueOf(HttpServletResponse.SC_FORBIDDEN));
        baseResponse.setDescription("forbidden Access");
        baseResponse.setAdditionalEntity(null);

        byte[] responseToSend = restResponseByte(baseResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getOutputStream().write(responseToSend);
    }

    private byte[] restResponseByte(BaseResponse errorResponse) throws IOException {
        String serialized = new ObjectMapper().writeValueAsString(errorResponse);
        return serialized.getBytes();
    }
}