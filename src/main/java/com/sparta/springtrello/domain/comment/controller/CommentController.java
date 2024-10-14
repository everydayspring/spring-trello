package com.sparta.springtrello.domain.comment.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.domain.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
}
