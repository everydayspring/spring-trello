package com.sparta.springtrello.domain.board.controller;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.board.dto.request.BoardSaveRequestDto;
import com.sparta.springtrello.domain.board.dto.request.BoardUpdateRequestDto;
import com.sparta.springtrello.domain.board.dto.request.GetBoardsRequestDto;
import com.sparta.springtrello.domain.board.service.BoardService;
import com.sparta.springtrello.domain.common.dto.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<ApiResponse<?>> saveBoard(
            @RequestBody BoardSaveRequestDto boardSaveRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(
                ApiResponse.success(boardService.saveBoards(boardSaveRequestDto, authUser)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getBoards(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody @Valid GetBoardsRequestDto request) {
        return ResponseEntity.ok(
                ApiResponse.success(boardService.getBoards(authUser, request.getWorkspaceId())));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getBoard(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(ApiResponse.success(boardService.getBoard(id, authUser)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardUpdateRequestDto boardUpdateRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(
                ApiResponse.success(boardService.updateBoard(id, boardUpdateRequestDto, authUser)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteBoard(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        boardService.deleteBoard(id, authUser);
        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
