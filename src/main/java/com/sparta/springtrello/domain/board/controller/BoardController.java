package com.sparta.springtrello.domain.board.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.domain.board.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
}
