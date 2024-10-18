package com.sparta.springtrello.domain.board.entitiy;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "boards")
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String background;
    private Long workspaceId;

    public Board(String name, String background, Long workspaceId) {
        this.name = name;
        this.background = background;
        this.workspaceId = workspaceId;
    }

    public void update(String name, String background) {
        this.name = name;
        this.background = background;
    }
}
