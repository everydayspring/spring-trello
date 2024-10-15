package com.sparta.springtrello.domain.card.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.sparta.springtrello.domain.card.dto.CardRequestDto;
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
    public ResponseEntity<Card> createCard(
            @RequestBody CardRequestDto cardRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        Card createdCard = cardService.createCard(authUser, cardRequestDto);
        return ResponseEntity.ok(createdCard);
    }

    // 조회
    @GetMapping("/{listId}")
    public ResponseEntity<List<Card>> getCardsByListId(@PathVariable Long listId) {
        List<Card> cards = cardService.findAllByListId(listId);
        return ResponseEntity.ok(cards);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<Card> updateCard(
            @PathVariable Long id,
            @RequestBody CardRequestDto cardRequestDto,
            @AuthenticationPrincipal AuthUser authUser) {
        Card updatedCard = cardService.updateCard(id, authUser, cardRequestDto);
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
