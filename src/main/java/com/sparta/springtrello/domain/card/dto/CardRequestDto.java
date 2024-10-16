package com.sparta.springtrello.domain.card.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardRequestDto {
    private String name;
    // private Long sequence;
    private String description;
    private LocalDateTime dueDate;
    private Long managerId;
    private Long listId;
}
