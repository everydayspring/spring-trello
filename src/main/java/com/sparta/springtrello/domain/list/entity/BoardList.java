package com.sparta.springtrello.domain.list.entity;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "lists")
public class BoardList extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Long sequence; // 리스트의 순서
    private Long boardId;

    public BoardList(String name, Long sequence, Long boardId) {
        this.name = name;
        this.sequence = sequence;
        this.boardId = boardId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }
}
