package com.sparta.springtrello.domain.card.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;

import com.sparta.springtrello.domain.card.entity.Card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GetCardsDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        @NotNull private Long listId;
    }

    @Getter
    public static class Response {
        private final Long id;
        private final Long sequence;
        private final String name;
        private final LocalDateTime dueDate;

        public Response(Card card) {
            this.id = card.getId();
            this.sequence = card.getSequence();
            this.name = card.getName();
            this.dueDate = card.getDueDate();
        }
    }
}
