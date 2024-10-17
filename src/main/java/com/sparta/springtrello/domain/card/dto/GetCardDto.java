package com.sparta.springtrello.domain.card.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.entity.CardLog;
import com.sparta.springtrello.domain.comment.entity.Comment;

import lombok.Getter;

public class GetCardDto {

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

        private final List<CardLog> cardLogs;
        private final List<Comment> comments;

        public Response(Card card, List<CardLog> cardLogs, List<Comment> comments) {
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
            this.cardLogs = cardLogs;
            this.comments = comments;
        }
    }
}
