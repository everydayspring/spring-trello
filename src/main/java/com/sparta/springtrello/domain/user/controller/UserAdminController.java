package com.sparta.springtrello.domain.user.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.ServerException;
import com.sparta.springtrello.domain.user.dto.request.UserRoleChangeRequest;
import com.sparta.springtrello.domain.user.service.UserAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @PatchMapping("/admin/users/{userId}")
    public ResponseEntity<ApiResponse<?>> changeUserRole(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable long userId,
            @RequestBody UserRoleChangeRequest userRoleChangeRequest) {

        if (!userAdminService.isAdmin(authUser)) {
            throw new ServerException("권한이 없습니다.");
        }

        userAdminService.changeUserRole(userId, userRoleChangeRequest);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
