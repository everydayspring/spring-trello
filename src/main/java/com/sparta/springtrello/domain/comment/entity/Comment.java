package com.sparta.springtrello.domain.comment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "comments")
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emoji;
    private String content;
    private Long cardId;
    private Long workspaceId;
    private Long userId;

    public Comment(String emoji, String content, Long cardId, Long workspaceId, Long userId) {
        this.emoji = emoji;
        this.content = content;
        this.cardId = cardId;
        this.workspaceId = workspaceId;
        this.userId = userId;
    }

    public void update(String emoji, String content) {
        this.emoji = emoji;
        this.content = content;
    }
}
