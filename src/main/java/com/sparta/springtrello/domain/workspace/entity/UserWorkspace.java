package com.sparta.springtrello.domain.workspace.entity;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "user_workspace")
public class UserWorkspace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long workspaceId;

    public UserWorkspace(Long userId, Long workspaceId) {
        this.userId = userId;
        this.workspaceId = workspaceId;
    }
}
