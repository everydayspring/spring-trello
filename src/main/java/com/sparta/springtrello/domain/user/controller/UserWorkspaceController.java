package com.sparta.springtrello.domain.user.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.workspace.dto.AddUserDto;
import com.sparta.springtrello.domain.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserWorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * 멤버 추가
     *
     * @param authUser
     * @param request
     * @return null
     */
    @PostMapping("/members")
    public ResponseEntity<ApiResponse<?>> addMember(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid AddUserDto.Request request) {

        workspaceService.addMember(
                authUser, request.getWorkspaceId(), request.getEmail(), request.getRole());

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
