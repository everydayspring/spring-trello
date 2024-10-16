package com.sparta.springtrello.domain.list.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ListRequestDto {
    private String name;
    private Long boardId;

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }
}
