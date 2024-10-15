package com.sparta.springtrello.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class GetBoardsRequestDto {
    @NotNull private Long workspaceId;
}
