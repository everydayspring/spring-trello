package com.sparta.springtrello.domain.card.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "cards")
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long sequence;
    private String description;
    private LocalDateTime dueDate;
    private Long managerId;
    private Long listId;

    private String fileName;
    private String fileUrl;

    public Card(
            String name,
            Long sequence,
            String description,
            LocalDateTime dueDate,
            Long managerId,
            Long listId,
            String fileName,
            String fileUrl) {
        this.name = name;
        this.sequence = sequence;
        this.description = description;
        this.dueDate = dueDate;
        this.managerId = managerId;
        this.listId = listId;
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public void updateCard(
            String name,
            Long sequence,
            String description,
            LocalDateTime dueDate,
            Long managerId,
            Long listId) {
        this.name = name;
        this.sequence = sequence;
        this.description = description;
        this.dueDate = dueDate;
        this.managerId = managerId;
        this.listId = listId;
    }

    public void addFile(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

    public void changeSequence(Long sequence) {
        this.sequence = sequence;
    }
}
