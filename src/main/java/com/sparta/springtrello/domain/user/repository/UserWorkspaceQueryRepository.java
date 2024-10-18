package com.sparta.springtrello.domain.user.repository;

import java.util.List;

import com.sparta.springtrello.domain.workspace.entity.Workspace;

public interface UserWorkspaceQueryRepository {
    List<Workspace> findAllWorkspacesByUserId(Long userId);
}
