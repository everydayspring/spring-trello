package com.sparta.springtrello.domain.user.controller;

import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.ServerException;
import com.sparta.springtrello.domain.user.dto.request.UserRoleChangeRequest;
import com.sparta.springtrello.domain.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserAdminService userAdminService;

    @PatchMapping("/admin/users/{userId}")
    public void changeUserRole(@AuthenticationPrincipal AuthUser authUser, @PathVariable long userId, @RequestBody UserRoleChangeRequest userRoleChangeRequest) {
        if (!userAdminService.isAdmin(authUser)) {
            throw new ServerException("권한이 없습니다.");
        }
        userAdminService.changeUserRole(userId, userRoleChangeRequest);
    }
}
