package com.sparta.springtrello.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GetBoardsRequestDto {
    @NotNull private final Long WorkspaceId;
}
