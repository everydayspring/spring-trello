package com.sparta.springtrello.domain.comment.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.request.CommentUpdateRequest;
import com.sparta.springtrello.domain.comment.service.CommentService;
import com.sparta.springtrello.domain.common.dto.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments")
    public ResponseEntity<ApiResponse<?>> saveComment(
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CommentRequest commentRequest) {

        return ResponseEntity.ok(
                ApiResponse.success(commentService.saveComment(authUser, commentRequest)));
    }

    @PutMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<?>> updateComment(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthUser authUser,
            @Valid @RequestBody CommentUpdateRequest commentUpdateRequest) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        commentService.updateComment(id, authUser, commentUpdateRequest)));
    }

    @DeleteMapping("/comments/{id}")
    public ResponseEntity<ApiResponse<?>> deleteComment(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {

        commentService.deleteComment(id, authUser);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
