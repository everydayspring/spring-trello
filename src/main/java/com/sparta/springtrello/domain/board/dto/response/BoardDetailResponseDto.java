package com.sparta.springtrello.domain.board.dto.response;

import lombok.Getter;

@Getter
public class BoardDetailResponseDto {

    private final Long id;
    private final String name;
    private final String background;
    private final Long workspaceId;

    public BoardDetailResponseDto(Long id, String name, String background, Long workspaceId) {
        this.id = id;
        this.name = name;
        this.background = background;
        this.workspaceId = workspaceId;
    }
}
