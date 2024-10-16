package com.sparta.springtrello.domain.user.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.user.dto.request.DeleteUserRequest;
import com.sparta.springtrello.domain.user.dto.request.UserChangePasswordRequest;
import com.sparta.springtrello.domain.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<?>> getUser(@PathVariable long userId) {

        return ResponseEntity.ok(ApiResponse.success(userService.getUser(userId)));
    }

    @PutMapping("/users")
    public ResponseEntity<ApiResponse<?>> changePassword(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody UserChangePasswordRequest userChangePasswordRequest) {

        userService.changePassword(authUser.getId(), userChangePasswordRequest);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }

    @DeleteMapping("/users")
    public ResponseEntity<ApiResponse<?>> deleteUser(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody DeleteUserRequest deleteUserRequest) {

        userService.deleteUser(authUser, deleteUserRequest);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
