package com.sparta.springtrello.domain.comment.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequest {

    @NotNull(message = "카드 ID는 필수입니다.")
    private Long cardId;
    private String emoji;
    @NotNull(message = "내용은 필수입니다.")
    private String content;
}
