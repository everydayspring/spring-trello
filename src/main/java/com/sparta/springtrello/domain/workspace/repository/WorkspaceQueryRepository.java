package com.sparta.springtrello.domain.workspace.repository;

public interface WorkspaceQueryRepository {
    void deleteWorkspaceWithAllData(Long workspaceId);
}
