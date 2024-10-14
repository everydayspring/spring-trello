package com.sparta.springtrello.domain.list.controller;

import com.sparta.springtrello.domain.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ListController {

    private final ListService listService;
}
