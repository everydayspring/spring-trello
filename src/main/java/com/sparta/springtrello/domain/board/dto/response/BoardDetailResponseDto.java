package com.sparta.springtrello.domain.board.dto.response;

import lombok.Getter;

@Getter
public class BoardDetailResponseDto {

    private final String name;
    private final String background;
    private final Long workspaceId;

    public BoardDetailResponseDto(String name, String background, Long workspaceId) {
        this.name = name;
        this.background = background;
        this.workspaceId = workspaceId;
    }
}
