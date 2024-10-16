package com.sparta.springtrello.domain.board.dto.response;

import java.util.List;

import lombok.Getter;

@Getter
public class BoardListDetailResponseDto {
    private final Long id;
    private final String name;
    private final Long sequence;
    private final List<BoardCardDetailResponseDto> cards;

    public BoardListDetailResponseDto(
            Long id, String name, Long sequence, List<BoardCardDetailResponseDto> cards) {
        this.id = id;
        this.name = name;
        this.sequence = sequence;
        this.cards = cards;
    }
}
