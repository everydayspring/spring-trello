package com.sparta.springtrello.domain.search.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.search.dto.CardSearchRequestDto;
import com.sparta.springtrello.domain.search.service.CardSearchService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class CardSearchController {

    private final CardSearchService cardSearchService;

    @GetMapping
    public Page<Card> searchCards(CardSearchRequestDto searchDto, Pageable pageable) {
        return cardSearchService.searchCards(searchDto, pageable);
    }
}
