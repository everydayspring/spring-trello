package com.sparta.springtrello.domain.list.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.list.dto.request.ListRequestDto;
import com.sparta.springtrello.domain.list.dto.request.ListSwapRequestDto;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.service.ListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lists")
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;

    // 생성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createList(
            @RequestBody ListRequestDto listRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {

        BoardList createdList = listService.createList(authUser, listRequestDto);

        return ResponseEntity.ok(ApiResponse.success(createdList));
    }

    // 조회
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getLists(
            @RequestParam Long boardId, @AuthenticationPrincipal AuthUser authUser) {

        List<BoardList> boardLists = listService.getListsByBoardId(boardId, authUser);

        return ResponseEntity.ok(ApiResponse.success(boardLists));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateList(
            @PathVariable Long id,
            @RequestBody ListRequestDto listRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {

        BoardList updatedList = listService.updateList(id, listRequestDto, authUser);

        return ResponseEntity.ok(ApiResponse.success(updatedList));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteList(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {

        listService.deleteList(id, authUser);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }

    @PutMapping
    public ResponseEntity<ApiResponse<?>> swapList(
            @RequestBody ListSwapRequestDto listSwapRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {

        listService.swapList(
                listSwapRequestDto.getList1(), listSwapRequestDto.getList2(), authUser);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
