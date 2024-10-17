package com.sparta.springtrello.domain.search.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardSearchRequestDto {
    private String name;
    private String description;
    private LocalDateTime dueDate;
    private String managerEmail;
    private Long boardId;
}
