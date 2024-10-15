package com.sparta.springtrello.domain.workspace.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.workspace.dto.CreateWorkspaceDto;
import com.sparta.springtrello.domain.workspace.dto.UpdateWorkspaceDto;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    /**
     * 워크스페이스 생성
     *
     * @param authUser
     * @param request
     * @return CreateWorkspaceDto.Response
     */
    @PostMapping
    public ResponseEntity<ApiResponse<?>> save(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid CreateWorkspaceDto.Request request) {

        Workspace workspace =
                workspaceService.save(
                        authUser,
                        request.getManagerId(),
                        request.getName(),
                        request.getDescription());

        return ResponseEntity.ok(ApiResponse.success(new CreateWorkspaceDto.Response(workspace)));
    }

    /**
     * 워크스페이스 조회
     *
     * @param authUser
     * @return List<Workspace>
     */
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getWorkspaces(
            @AuthenticationPrincipal AuthUser authUser) {
        List<Workspace> workspaces = workspaceService.getWorkspaces(authUser.getId());
        return ResponseEntity.ok(ApiResponse.success(workspaces));
    }

    /**
     * 워크스페이스 수정
     *
     * @param authUser
     * @param id
     * @param request
     * @return UpdateWorkspaceDto.Response
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long id,
            @RequestBody @Valid UpdateWorkspaceDto.Request request) {

        Workspace workspace =
                workspaceService.update(authUser, id, request.getName(), request.getDescription());

        return ResponseEntity.ok(ApiResponse.success(new UpdateWorkspaceDto.Response(workspace)));
    }

    /**
     * 워크스페이스 삭제
     *
     * @param authUser
     * @param id
     * @return null
     */
    @DeleteMapping("{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @AuthenticationPrincipal AuthUser authUser, @PathVariable Long id) {
        workspaceService.delete(authUser, id);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
