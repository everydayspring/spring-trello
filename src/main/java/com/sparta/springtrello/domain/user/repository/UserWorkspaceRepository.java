package com.sparta.springtrello.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.user.entity.UserWorkspace;

public interface UserWorkspaceRepository
        extends JpaRepository<UserWorkspace, Long>, UserWorkspaceQueryRepository {
    Optional<UserWorkspace> findByUserIdAndWorkspaceId(Long id, Long workspaceId);
}
