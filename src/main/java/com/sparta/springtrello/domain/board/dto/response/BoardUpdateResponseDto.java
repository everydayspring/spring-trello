package com.sparta.springtrello.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class BoardUpdateResponseDto {
    private final String name;
    private final String background;
    private final Long workspaceId;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public BoardUpdateResponseDto(
            String name,
            String background,
            Long workspaceId,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt) {
        this.name = name;
        this.background = background;
        this.workspaceId = workspaceId;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
