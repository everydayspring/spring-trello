package com.sparta.springtrello.domain.list.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.card.entity.QCard;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.list.dto.request.ListRequestDto;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.entity.QBoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListService {

    private final ListRepository listRepository;
    private final JPAQueryFactory queryFactory;

    // 리스트 생성
    @Transactional
    public BoardList createList(AuthUser authUser, ListRequestDto listRequestDto) {
        // 읽기 전용 사용자 x(수정이랑 삭제에도 넣어야함)
        //        if (authUser.isReadOnly()) {
        //            throw new IllegalArgumentException("읽기 전용 사용자는 리스트를 생성할 수 없습니다.");
        //        }

        // 새로운 리스트 생성
        BoardList boardList =
                new BoardList(
                        listRequestDto.getName(),
                        listRequestDto.getSequence(),
                        listRequestDto.getBoardId());

        // 리스트 저장
        return listRepository.save(boardList);
    }

    // 리스트 조회
    public List<BoardList> getListsByBoardId(Long boardId, AuthUser authUser) {
        QBoardList boardList = QBoardList.boardList;

        // 보드에 속한 모든 리스트 조회
        return queryFactory.selectFrom(boardList).where(boardList.boardId.eq(boardId)).fetch();
    }


    // 리스트 수정
    @Transactional
    public BoardList updateList(Long listId, ListRequestDto listRequestDto, AuthUser authUser) {

        // 리스트 조회
        BoardList boardList =
                listRepository
                        .findById(listId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));

        // 리스트 정보 업데이트
        boardList.setName(listRequestDto.getName());
        boardList.setSequence(listRequestDto.getSequence());

        return listRepository.save(boardList);
    }


    // 리스트 삭제
    public void deleteList(Long listId, AuthUser authUser) {
        // 리스트 조회
        BoardList boardList =
                listRepository
                        .findById(listId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));

        QCard card = QCard.card;
        queryFactory.delete(card).where(card.listId.eq(listId)).execute();

        listRepository.delete(boardList);
    }

}
