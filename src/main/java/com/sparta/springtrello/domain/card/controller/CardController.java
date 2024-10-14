package com.sparta.springtrello.domain.card.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.domain.card.service.CardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;
}
