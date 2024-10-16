package com.sparta.springtrello.domain.board.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class BoardDetailCardListResponseDto {
    private final Long id;
    private final String name;
    private final String background;
    private final Long workspaceId;
    private final List<BoardListDetailResponseDto> lists;

    public BoardDetailCardListResponseDto(
            Long id,
            String name,
            String background,
            Long workspaceId,
            List<BoardListDetailResponseDto> lists) {
        this.id = id;
        this.name = name;
        this.background = background;
        this.workspaceId = workspaceId;
        this.lists = lists;
    }
}
