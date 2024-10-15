package com.sparta.springtrello.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class BoardSaveResponseDto {
    private final Long id;
    private final Long workspaceId;
    private final String name;
    private final String background;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public BoardSaveResponseDto(
            Long id,
            Long workspaceId,
            String name,
            String background,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.id = id;
        this.workspaceId = workspaceId;
        this.name = name;
        this.background = background;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
