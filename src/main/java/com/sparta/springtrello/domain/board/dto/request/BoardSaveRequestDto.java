package com.sparta.springtrello.domain.board.dto.request;

import lombok.Getter;

@Getter
public class BoardSaveRequestDto {
    private String name;
    private String background;
    private Long workspaceId;
}
