package com.sparta.springtrello.domain.comment.dto.request;

import lombok.Getter;

@Getter
public class CommentUpdateRequest {

    private String emoji;
    private String content;
}
