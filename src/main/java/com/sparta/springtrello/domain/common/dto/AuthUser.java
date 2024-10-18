package com.sparta.springtrello.domain.common.dto;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.sparta.springtrello.domain.user.enums.UserRole;

import lombok.Getter;

@Getter
public class AuthUser {

    private final Long id;
    private final String email;
    private final Collection<? extends GrantedAuthority> authorities;
    private final String slackId;

    public AuthUser(Long id, String email, UserRole userRole, String slackId) {
        this.id = id;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(userRole.name()));
        this.slackId = slackId;
    }
}
