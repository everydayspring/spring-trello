package com.sparta.springtrello.domain.board.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.springtrello.domain.board.dto.request.BoardSaveRequestDto;
import com.sparta.springtrello.domain.board.dto.request.BoardUpdateRequestDto;
import com.sparta.springtrello.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.springtrello.domain.board.dto.response.BoardSaveResponseDto;
import com.sparta.springtrello.domain.board.dto.response.BoardUpdateResponseDto;
import com.sparta.springtrello.domain.board.service.BoardService;
import com.sparta.springtrello.domain.common.dto.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    @PostMapping
    public ResponseEntity<BoardSaveResponseDto> saveBoard(
            @RequestBody BoardSaveRequestDto boardSaveRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(boardService.saveBoards(boardSaveRequestDto, authUser));
    }

    @GetMapping
    public ResponseEntity<List<BoardDetailResponseDto>> getBoards() {
        return ResponseEntity.ok(boardService.getBoards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailResponseDto> getBoard(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(boardService.getBoard(id, authUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardUpdateResponseDto> updateBoard(
            @PathVariable Long id,
            @RequestBody BoardUpdateRequestDto boardUpdateRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(boardService.updateBoard(id, boardUpdateRequestDto, authUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBoard(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        boardService.deleteBoard(id, authUser);
        return ResponseEntity.ok().build();
    }
}
