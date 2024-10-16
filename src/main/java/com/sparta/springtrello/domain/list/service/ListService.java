package com.sparta.springtrello.domain.list.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.entity.QCard;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.list.dto.request.ListRequestDto;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.entity.QBoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.user.entity.UserWorkspace;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final JPAQueryFactory queryFactory;

    // 리스트 생성
    @Transactional
    public BoardList createList(AuthUser authUser, ListRequestDto listRequestDto) {

        Board board =
                boardRepository
                        .findById(listRequestDto.getBoardId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));
        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 리스트를 생성할 수 없습니다.");
        }

        Long currentListCount = listRepository.countByBoardId(board.getId());

        // 새로운 리스트 생성
        BoardList boardList =
                new BoardList(
                        listRequestDto.getName(),
                        currentListCount + 1,
                        listRequestDto.getBoardId());

        // 리스트 저장
        return listRepository.save(boardList);
    }

    // 리스트 조회
    public List<BoardList> getListsByBoardId(Long boardId, AuthUser authUser) {
        Board board =
                boardRepository
                        .findById(boardId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));
        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 리스트를 수정할 수 없습니다.");
        }

        QBoardList boardList = QBoardList.boardList;
        // 보드에 속한 모든 리스트 조회
        return queryFactory.selectFrom(boardList).where(boardList.boardId.eq(boardId)).fetch();
    }

    // 리스트 수정
    @Transactional
    public BoardList updateList(Long listId, ListRequestDto listRequestDto, AuthUser authUser) {
        BoardList boardList =
                listRepository
                        .findById(listId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 보드를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 리스트를 수정할 수 없습니다.");
        }

        // 리스트 정보 업데이트(보드 아이디는 변경 불가)
        boardList.setName(listRequestDto.getName());

        return listRepository.save(boardList);
    }

    // 리스트 삭제
    @Transactional
    public void deleteList(Long listId, AuthUser authUser) {

        // 리스트 조회
        BoardList boardList =
                listRepository
                        .findById(listId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 리스트가 속한 보드를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 리스트를 삭제할 수 없습니다.");
        }

        QCard card = QCard.card;
        queryFactory.delete(card).where(card.listId.eq(listId)).execute();

        listRepository.delete(boardList);

        // 보드에 속한 남은 리스트들의 순서를 다시 설정
        List<BoardList> reorder =
                queryFactory
                        .selectFrom(QBoardList.boardList)
                        .where(
                                QBoardList.boardList
                                        .boardId
                                        .eq(boardList.getBoardId())
                                        .and(
                                                QBoardList.boardList.sequence.gt(
                                                        boardList.getSequence())))
                        // 리스트들만 조회
                        .orderBy(QBoardList.boardList.sequence.asc()) // 기존 순서대로 정렬
                        .fetch();

        // 순서를 다시 설정
        for (int i = 0; i < reorder.size(); i++) {
            BoardList list = reorder.get(i);
            list.setSequence(list.getSequence() - 1); // 순서를 1씩 줄임
            listRepository.save(list); // 업데이트된 순서 저장
        }
    }

    @Transactional
    public void swapList(Long list1Id, Long list2Id, AuthUser authUser) {

        BoardList list1 =
                listRepository
                        .findById(list1Id)
                        .orElseThrow(() -> new IllegalArgumentException("List 1을 찾을 수 없습니다."));

        BoardList list2 =
                listRepository
                        .findById(list2Id)
                        .orElseThrow(() -> new IllegalArgumentException("List 2를 찾을 수 없습니다."));

        if (!list1.getBoardId().equals(list2.getBoardId())) {
            throw new InvalidRequestException("리스트는 같은 보드에 있어야 합니다.");
        }

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), list1.getBoardId())
                        .orElseThrow(() -> new InvalidRequestException("해당 보드에 대한 권한이 없습니다."));

        Long tempSequence = list1.getSequence();
        list1.setSequence(list2.getSequence());
        list2.setSequence(tempSequence);

        listRepository.save(list1);
        listRepository.save(list2);
    }
}
