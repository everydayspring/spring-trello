package com.sparta.springtrello.domain.user.entity;

import jakarta.persistence.*;

import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;

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
    private WorkspaceUserRole workspaceUserRole;

    public UserWorkspace(Long userId, Long workspaceId, WorkspaceUserRole workspaceUserRole) {
        this.userId = userId;
        this.workspaceId = workspaceId;
        this.workspaceUserRole = workspaceUserRole;
    }

    public void changeUserRole(WorkspaceUserRole newWorkspaceUserRole) {
        this.workspaceUserRole = newWorkspaceUserRole;
    }
}
