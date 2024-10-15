package com.sparta.springtrello.domain.list.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.list.dto.request.ListRequestDto;
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
    public ResponseEntity<BoardList> createList(
            @RequestBody ListRequestDto listRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        BoardList createdList = listService.createList(authUser, listRequestDto);
        return ResponseEntity.ok(createdList);
    }

    // 조회
    @GetMapping
    public ResponseEntity<List<BoardList>> getAllLists(
            @RequestParam Long boardId, @AuthenticationPrincipal AuthUser authUser) {
        List<BoardList> lists = listService.getListsByBoardId(boardId, authUser);
        return ResponseEntity.ok(lists);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<BoardList> updateList(
            @PathVariable Long id,
            @RequestBody ListRequestDto listRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        BoardList updatedList = listService.updateList(id, listRequestDto, authUser);
        return ResponseEntity.ok(updatedList);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteList(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        listService.deleteList(id, authUser);
        return ResponseEntity.noContent().build();
    }

}
