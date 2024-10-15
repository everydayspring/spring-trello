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

    public Card(
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

    public void changeManager(Long managerId) {
        this.managerId = managerId;
    }

    public void updateCard(String name, String description, LocalDateTime dueDate, Long managerId) {
        this.name = name;
        this.description = description;
        this.dueDate = dueDate;
        this.managerId = managerId;
    }

    public void setListId(Long listId) {
        this.listId = listId;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
