package com.sparta.springtrello.domain.common.dto;

import com.sparta.springtrello.domain.user.enums.UserRole;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;


    public AuthUser(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
    }
}
