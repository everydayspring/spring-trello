package com.sparta.springtrello.domain.workspace.controller;

import org.springframework.web.bind.annotation.RestController;

import com.sparta.springtrello.domain.workspace.service.WorkspaceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
}
