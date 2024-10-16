package com.sparta.springtrello.domain.board.dto.response;

import java.time.LocalDateTime;

import lombok.Getter;

@Getter
public class BoardCardDetailResponseDto {
    private final Long id;
    private final String name;
    private final String description;
    private final LocalDateTime dueDate;
    private final Long managerId;

    public BoardCardDetailResponseDto(
            Long id, String name, String description, LocalDateTime dueDate, Long managerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.managerId = managerId;
    }
}
