package com.sparta.springtrello.domain.card.controller;

import java.io.IOException;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.springtrello.config.ApiResponse;
import com.sparta.springtrello.domain.card.dto.CreateCardDto;
import com.sparta.springtrello.domain.card.dto.GetCardDto;
import com.sparta.springtrello.domain.card.dto.GetCardsDto;
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

    // 다건 조회
    @GetMapping
    public ResponseEntity<ApiResponse<?>> getCards(
            @Valid @RequestBody GetCardsDto.Request request,
            @AuthenticationPrincipal AuthUser authUser) {

        List<Card> cards = cardService.getCards(request.getListId(), authUser);

        List<GetCardsDto.Response> cardResponses =
                cards.stream().map(GetCardsDto.Response::new).toList();

        return ResponseEntity.ok(ApiResponse.success(cardResponses));
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getCard(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {

        cardService.incrementCardViewCount(id, authUser.getId());
        GetCardDto.Response response = cardService.getCard(id, authUser);

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateCard(
            @PathVariable Long id,
            @RequestPart("data") CreateCardDto.Request request,
            @RequestPart("file") MultipartFile file,
            @AuthenticationPrincipal AuthUser authUser)
            throws IOException {

        Card card = cardService.updateCard(id, authUser, request, file);

        return ResponseEntity.ok(ApiResponse.success(new CreateCardDto.Response(card)));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteCard(
            @PathVariable Long id, @AuthenticationPrincipal AuthUser authUser) {

        cardService.deleteCard(id, authUser);

        return ResponseEntity.ok(ApiResponse.successWithNoContent());
    }
}
