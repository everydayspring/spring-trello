package com.sparta.springtrello.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.auth.exception.AuthException;
import com.sparta.springtrello.domain.board.dto.request.BoardSaveRequestDto;
import com.sparta.springtrello.domain.board.dto.request.BoardUpdateRequestDto;
import com.sparta.springtrello.domain.board.dto.response.BoardDetailResponseDto;
import com.sparta.springtrello.domain.board.dto.response.BoardSaveResponseDto;
import com.sparta.springtrello.domain.board.dto.response.BoardUpdateResponseDto;
import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    @Transactional
    public BoardSaveResponseDto saveBoards(
            BoardSaveRequestDto boardSaveRequestDto, AuthUser authUser) {

        if (authUser == null) {
            throw new AuthException("로그인이 필요합니다.");
        }

        // 현재 사용자를 조회
        User user =
                userRepository
                        .findById(authUser.getId())
                        .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // 사용자 권한 체크
        if (!UserRole.ROLE_ADMIN.equals(user.getUserRole())) {
            throw new AuthException("읽기 전용 권한으로 보드를 생성할 수 없습니다.");
        }

        // 보드 이름 체크
        if (boardSaveRequestDto.getName() == null || boardSaveRequestDto.getName().isEmpty()) {
            throw new InvalidRequestException("보드 제목을 작성해 주십시오.");
        }

        // 워크스페이스 유효성 체크
        Workspace workspace =
                workspaceRepository
                        .findById(boardSaveRequestDto.getWorkspaceId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 워크스페이스가 존재하지 않습니다."));

        // 보드 생성 및 저장
        Board board =
                new Board(
                        boardSaveRequestDto.getName(),
                        boardSaveRequestDto.getBackground(),
                        workspace.getId() // 워크스페이스 ID를 직접 전달
                        );

        Board savedBoard = boardRepository.save(board); // 제대로 된 보드를 저장

        // 저장된 보드 정보를 DTO로 반환
        return new BoardSaveResponseDto(
                savedBoard.getId(),
                savedBoard.getWorkspaceId(),
                savedBoard.getName(),
                savedBoard.getBackground(),
                savedBoard.getCreatedAt(),
                savedBoard.getModifiedAt());
    }

    public List<BoardDetailResponseDto> getBoards(AuthUser authUser, Long id) {

        // workspace 멤버인지 조회
        if (userWorkspaceRepository.findByUserIdAndWorkspaceId(authUser.getId(), id).isEmpty()) {
            throw new InvalidRequestException("워크스페이스 멤버가 아닙니다");
        }

        // workspace 검증
        if (workspaceRepository.findById(id).isEmpty()) {
            throw new InvalidRequestException("workspace not found");
        }

        List<Board> boardList = boardRepository.findByWorkspaceId(id);

        return boardList.stream()
                .map(
                        board ->
                                new BoardDetailResponseDto(
                                        board.getId(),
                                        board.getName(),
                                        board.getBackground(),
                                        board.getWorkspaceId()))
                .collect(Collectors.toList());
    }

    public BoardDetailResponseDto getBoard(Long id, AuthUser authUser) {

        Board board =
                boardRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));

        if (userWorkspaceRepository
                .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                .isEmpty()) {
            throw new InvalidRequestException("워크스페이스 멤버가 아닙니다");
        }

        return new BoardDetailResponseDto(
                board.getId(), board.getName(), board.getBackground(), board.getWorkspaceId());
    }

    @Transactional
    public BoardUpdateResponseDto updateBoard(
            Long id, BoardUpdateRequestDto boardUpdateRequestDto, AuthUser authUser) {

        User user =
                userRepository
                        .findById(authUser.getId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (!UserRole.ROLE_ADMIN.equals(user.getUserRole())) {
            throw new AuthException("읽기 전용 권한으로 보드를 수정할 수 없습니다.");
        }

        Board board =
                boardRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));

        board.update(boardUpdateRequestDto.getName(), boardUpdateRequestDto.getBackground());

        return new BoardUpdateResponseDto(
                board.getName(),
                board.getBackground(),
                board.getWorkspaceId(),
                board.getCreatedAt(),
                board.getModifiedAt());
    }

    @Transactional
    public void deleteBoard(Long id, AuthUser authUser) {

        User user =
                userRepository
                        .findById(authUser.getId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (!UserRole.ROLE_ADMIN.equals(user.getUserRole())) {
            throw new AuthException("읽기 전용 권한으로 보드를 삭제할 수 없습니다.");
        }
        Board board =
                boardRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));

        boardRepository.delete(board);
    }
}
