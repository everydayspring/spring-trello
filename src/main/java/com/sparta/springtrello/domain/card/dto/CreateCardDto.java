package com.sparta.springtrello.domain.card.dto;

import java.time.LocalDateTime;

import com.sparta.springtrello.domain.card.entity.Card;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CreateCardDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String description;
        private LocalDateTime dueDate;
        private Long managerId;
        private Long listId;
    }

    @Getter
    public static class Response {
        private final Long id;
        private final String name;
        private final String description;
        private final LocalDateTime dueDate;
        private final Long managerId;
        private final Long listId;
        private final String fileName;
        private final String fileUrl;
        private final LocalDateTime createdAt;
        private final LocalDateTime modifiedAt;

        public Response(Card card) {
            this.id = card.getId();
            this.name = card.getName();
            this.description = card.getDescription();
            this.dueDate = card.getDueDate();
            this.managerId = card.getManagerId();
            this.listId = card.getListId();
            this.fileName = card.getFileName();
            this.fileUrl = card.getFileUrl();
            this.createdAt = card.getCreatedAt();
            this.modifiedAt = card.getModifiedAt();
        }
    }
}
