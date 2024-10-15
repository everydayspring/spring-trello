package com.sparta.springtrello.domain.list.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.domain.list.service.ListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;
}
