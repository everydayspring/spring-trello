package com.sparta.springtrello.domain.list.entity;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "lists")
public class List extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long sequence;
    private Long boardId;

    public List(String name, Long sequence, Long boardId) {
        this.name = name;
        this.sequence = sequence;
        this.boardId = boardId;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
