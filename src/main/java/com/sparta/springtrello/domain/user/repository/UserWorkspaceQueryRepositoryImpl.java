package com.sparta.springtrello.domain.user.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.user.entity.QUserWorkspace;
import com.sparta.springtrello.domain.workspace.entity.QWorkspace;
import com.sparta.springtrello.domain.workspace.entity.Workspace;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserWorkspaceQueryRepositoryImpl implements UserWorkspaceQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Workspace> findAllWorkspacesByUserId(Long userId) {
        QUserWorkspace userWorkspace = QUserWorkspace.userWorkspace;
        QWorkspace workspace = QWorkspace.workspace;

        // userId를 사용하여 해당 사용자가 속한 워크스페이스 목록 조회
        return queryFactory
                .select(workspace)
                .from(userWorkspace)
                .join(workspace)
                .on(userWorkspace.workspaceId.eq(workspace.id)) // 연관관계 없이 workspaceId로 조인
                .where(userWorkspace.userId.eq(userId))
                .fetch();
    }
}
