package com.sparta.springtrello.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.user.enums.UserRole;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    private String slackId;

    private boolean isDeleted = false;

    public User(String email, String password, UserRole userRole, String slackId) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
        this.slackId = slackId;
    }

    public User(String email, String password, UserRole userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    private User(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    public static User fromAuthUser(AuthUser authUser) {
        return new User(
                authUser.getId(),
                authUser.getEmail(),
                UserRole.of(String.valueOf(authUser.getAuthorities().stream().findFirst().get())));
    }

    public void changeIsDeleted() {
        this.isDeleted = true; // 탈퇴 처리
    }

    public boolean isDeleted() {
        return this.isDeleted; // 탈퇴 여부 확인
    }

    public void changePassword(String password) {
        this.password = password;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
