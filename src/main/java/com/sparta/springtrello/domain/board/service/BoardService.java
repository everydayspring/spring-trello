package com.sparta.springtrello.domain.board.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.auth.exception.AuthException;
import com.sparta.springtrello.domain.board.dto.request.BoardSaveRequestDto;
import com.sparta.springtrello.domain.board.dto.request.BoardUpdateRequestDto;
import com.sparta.springtrello.domain.board.dto.response.*;
import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
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
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final ListRepository listRepository;
    private final CardRepository cardRepository;

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

        if (userWorkspaceRepository.findByUserIdAndWorkspaceId(authUser.getId(), id).isEmpty()) {
            throw new InvalidRequestException("워크스페이스 멤버가 아닙니다");
        }

        if (workspaceRepository.findById(id).isEmpty()) {
            throw new InvalidRequestException("워크스페이스를 찾을 수 없습니다.");
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

    public BoardDetailCardListResponseDto getBoard(Long id, AuthUser authUser) {
        Board board =
                boardRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));

        if (userWorkspaceRepository
                .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                .isEmpty()) {
            throw new InvalidRequestException("워크스페이스 멤버가 아닙니다");
        }

        List<BoardList> boardLists = listRepository.findAllByBoardId(board.getId());

        List<BoardListDetailResponseDto> listDtos =
                boardLists.stream()
                        .map(
                                list -> {
                                    List<Card> cards = cardRepository.findAllByListId(list.getId());

                                    List<BoardCardDetailResponseDto> cardDtos =
                                            cards.stream()
                                                    .map(
                                                            card ->
                                                                    new BoardCardDetailResponseDto(
                                                                            card.getId(),
                                                                            card.getName(),
                                                                            card.getDescription(),
                                                                            card.getDueDate(),
                                                                            card.getManagerId()))
                                                    .collect(Collectors.toList());

                                    return new BoardListDetailResponseDto(
                                            list.getId(),
                                            list.getName(),
                                            list.getSequence(),
                                            cardDtos);
                                })
                        .collect(Collectors.toList());

        return new BoardDetailCardListResponseDto(
                board.getId(),
                board.getName(),
                board.getBackground(),
                board.getWorkspaceId(),
                listDtos);
    }

    @Transactional
    public BoardUpdateResponseDto updateBoard(
            Long id, BoardUpdateRequestDto boardUpdateRequestDto, AuthUser authUser) {

        User user =
                userRepository
                        .findById(authUser.getId())
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(
                                user.getId(), boardUpdateRequestDto.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 소속되어 있지 않습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AuthException("해당 워크스페이스를 수정할 권한이 없습니다.");
        }

        Board board =
                boardRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));

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
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("보드를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(user.getId(), board.getWorkspaceId())
                        .orElseThrow(() -> new AuthException("해당 워크스페이스에 소속되어 있지 않습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AuthException("해당 워크스페이스를 수정할 권한이 없습니다.");
        }

        boardRepository.delete(board);
    }
}
