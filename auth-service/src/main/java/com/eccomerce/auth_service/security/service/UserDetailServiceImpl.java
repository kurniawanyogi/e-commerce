package com.eccomerce.auth_service.security.service;

import com.eccomerce.auth_service.entity.Role;
import com.eccomerce.auth_service.entity.User;
import com.eccomerce.auth_service.exception.MainException;
import com.eccomerce.auth_service.model.StatusEnum;
import com.eccomerce.auth_service.repository.UserRepository;
import com.eccomerce.auth_service.security.entity.Authentication;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        if (!optionalUser.isPresent()) throw new MainException("400-VALIDATION", "Username or Password is incorrect");
        if (optionalUser.get().getStatus().equals(StatusEnum.INACTIVE.getAbbreviation()))
            throw new MainException("400-VALIDATION", "User Inactive, mohon hubungi admin");

        return new Authentication(optionalUser.get().getUsername(), optionalUser.get().getPassword(), true, true, true, true,
                getAuthorities(optionalUser.get()), optionalUser.get());
    }

    public Collection<GrantedAuthority> getAuthorities(User user) {
        Set<Role> roles = user.getRoles();
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        return authorities;
    }
}
