package com.sparta.springtrello.domain.workspace.service;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.entity.UserWorkspace;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    private final UserRepository userRepository;

    @Transactional
    public Workspace save(AuthUser authUser, Long managerId, String name, String description) {
        // ADMIN 검증을 위해 user를 받아옴
        User user =
                userRepository
                        .findById(authUser.getId())
                        .orElseThrow(() -> new InvalidRequestException("User not found"));

        // UserRole check
        if (!user.getUserRole().equals(UserRole.ROLE_ADMIN)) {
            throw new InvalidRequestException("User is not admin");
        }

        // 매니저가 지정되지 않았다면 authUser를 매니저로 지정
        if (Objects.isNull(managerId)) {
            managerId = user.getId();
        }

        Workspace workspace = new Workspace(managerId, name, description);

        workspaceRepository.save(workspace);

        // 관리자를 WORKSPACE 멤버로 등록
        userWorkspaceRepository.save(
                new UserWorkspace(managerId, workspace.getId(), WorkspaceUserRole.WORKSPACE));

        return workspace;
    }

    @Transactional
    public void addMember(AuthUser authUser, Long id, String email, WorkspaceUserRole role) {

        // workspace 조회
        Workspace workspace =
                workspaceRepository
                        .findById(id)
                        .orElseThrow(() -> new InvalidRequestException("Workspace not found"));

        // workspace 멤버인지 조회
        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), id)
                        .orElseThrow(() -> new InvalidRequestException("워크스페이스 멤버가 아닙니다"));

        // workspace 멤버 권환 확인
        if (!userWorkspace.getWorkspaceUserRole().equals(WorkspaceUserRole.WORKSPACE)) {
            throw new InvalidRequestException("권한이 없습니다.");
        }

        // 추가할 user 검증
        User addUser =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new InvalidRequestException("User not found"));

        // 중복 등록 검증
        if (userWorkspaceRepository.findByUserIdAndWorkspaceId(addUser.getId(), id).isPresent()) {
            throw new InvalidRequestException("이미 워크스페이스의 멤버입니다.");
        }

        // 멤버 권한 값이 없으면 기본값 지정
        if (Objects.isNull(role)) {
            role = WorkspaceUserRole.READ_ONLY;
        }

        userWorkspaceRepository.save(new UserWorkspace(addUser.getId(), workspace.getId(), role));
    }

    public List<Workspace> getWorkspaces(Long userId) {
        // queryDSL을 사용한 검색
        // workspace 권한까지 묶어서 넣고 싶은데 우선...
        return userWorkspaceRepository.findAllWorkspacesByUserId(userId);
    }

    @Transactional
    public Workspace update(AuthUser authUser, Long id, String name, String description) {
        // workspace 조회
        Workspace workspace =
                workspaceRepository
                        .findById(id)
                        .orElseThrow(() -> new InvalidRequestException("Workspace not found"));

        // workspace 멤버인지 조회
        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), id)
                        .orElseThrow(() -> new InvalidRequestException("워크스페이스 멤버가 아닙니다"));

        // workspace 멤버 권환 확인
        if (!userWorkspace.getWorkspaceUserRole().equals(WorkspaceUserRole.WORKSPACE)) {
            throw new InvalidRequestException("권한이 없습니다.");
        }

        workspace.updateWorkspace(name, description);

        return workspace;
    }

    @Transactional
    public void delete(AuthUser authUser, Long id) {
        // workspace 조회
        Workspace workspace =
                workspaceRepository
                        .findById(id)
                        .orElseThrow(() -> new InvalidRequestException("Workspace not found"));

        // workspace 멤버인지 조회
        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), id)
                        .orElseThrow(() -> new InvalidRequestException("워크스페이스 멤버가 아닙니다"));

        // workspace 멤버 권환 확인
        if (!userWorkspace.getWorkspaceUserRole().equals(WorkspaceUserRole.WORKSPACE)) {
            throw new InvalidRequestException("권한이 없습니다.");
        }

        workspaceRepository.deleteWorkspaceWithAllData(workspace.getId());
    }
}
