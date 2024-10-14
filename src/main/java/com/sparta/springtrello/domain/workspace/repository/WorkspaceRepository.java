package com.sparta.springtrello.domain.workspace.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.workspace.entity.Workspace;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {}
