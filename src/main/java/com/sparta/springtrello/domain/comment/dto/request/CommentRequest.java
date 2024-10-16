package com.sparta.springtrello.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentRequest {

    private Long cardId;
    private Long workspaceId;
    private String emoji;
    private String content;
}
