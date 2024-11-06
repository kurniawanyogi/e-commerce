package com.eccomerce.auth_service.security.entity;


import com.eccomerce.auth_service.entity.Role;
import com.eccomerce.auth_service.entity.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
public class Authentication extends org.springframework.security.core.userdetails.User implements UserDetails {
    private User user;

    public Authentication(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Authentication(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, User user) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.user = user;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Set<Role> roles = user.getRoles();
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for (Role role : roles) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
        }
        return authorities;
    }

    public String getUserId() {
        return user.getId();
    }
}
