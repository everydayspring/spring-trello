package com.sparta.springtrello.domain.card.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cardLogs")
public class CardLog extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cardId;

    private String name;
    private Long sequence;
    private String description;
    private LocalDateTime dueDate;
    private Long managerId;
    private Long listId;

    private String fileName;
    private String fileUrl;

    public CardLog(Card card) {
        this.cardId = card.getId();
        this.name = card.getName();
        this.sequence = card.getSequence();
        this.description = card.getDescription();
        this.dueDate = card.getDueDate();
        this.managerId = card.getManagerId();
        this.listId = card.getListId();
        this.fileName = card.getFileName();
        this.fileUrl = card.getFileUrl();
    }
}
