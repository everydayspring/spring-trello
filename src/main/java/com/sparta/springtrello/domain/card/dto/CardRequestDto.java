package com.sparta.springtrello.domain.card.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CardRequestDto {
    private String name;
    private Long sequence;
    private String description;
    private LocalDateTime dueDate;
    private Long managerId;
    private Long listId;
}
