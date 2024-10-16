package com.sparta.springtrello.domain.card.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.card.dto.CreateCardDto;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.service.CardService;
import com.sparta.springtrello.domain.common.dto.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    // 생성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createCard(
            @RequestPart("data") CreateCardDto.Request request,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal AuthUser authUser)
            throws IOException {

        Card card = cardService.createCard(authUser, request, file);

        return ResponseEntity.ok(ApiResponse.success(new CreateCardDto.Response(card)));
    }

    // 조회
    @GetMapping("/{listId}")
    public ResponseEntity<List<Card>> getCardsByListId(
            @PathVariable Long listId, @AuthenticationPrincipal AuthUser authUser) {
        List<Card> cards = cardService.findAllByListId(listId, authUser);
        return ResponseEntity.ok(cards);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(
            @PathVariable Long id,
            @ModelAttribute CreateCardDto.Request request,
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal AuthUser authUser)
            throws IOException {
        Card updatedCard = cardService.updateCard(id, authUser, request, file); // 파일 전달
        return ResponseEntity.ok(updatedCard);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCard(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {
        cardService.deleteCard(id, authUser);
        return ResponseEntity.noContent().build();
    }
}
