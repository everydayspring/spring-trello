package com.sparta.springtrello.domain.comment.dto.response;

import java.time.LocalDateTime;

import com.sparta.springtrello.domain.comment.entity.Comment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentResponse {

    private final Long id;
    private final Long cardId;
    private final Long userId;
    private final String emoji;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;

    public static CommentResponse entityToDto(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getCardId(),
                comment.getUserId(),
                comment.getEmoji(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getModifiedAt());
    }
}
