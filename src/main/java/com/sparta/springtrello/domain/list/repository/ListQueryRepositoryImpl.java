package com.sparta.springtrello.domain.list.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.card.entity.QCard;
import com.sparta.springtrello.domain.card.entity.QCardLog;
import com.sparta.springtrello.domain.comment.entity.QComment;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.entity.QBoardList;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ListQueryRepositoryImpl implements ListQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BoardList> findListsByBoardId(Long boardId) {
        QBoardList boardList = QBoardList.boardList;
        return queryFactory.selectFrom(boardList).where(boardList.boardId.eq(boardId)).fetch();
    }

    @Override
    public List<BoardList> findListsReorder(Long boardId, Long sequence) {
        QBoardList boardList = QBoardList.boardList;
        return queryFactory
                .selectFrom(boardList)
                .where(boardList.boardId.eq(boardId).and(boardList.sequence.gt(sequence)))
                .orderBy(boardList.sequence.asc())
                .fetch();
    }

    @Override
    public void deleteListWithCards(Long listId) {
        QCard card = QCard.card;
        QComment comment = QComment.comment;
        QCardLog cardLog = QCardLog.cardLog;

        List<Long> cardIds =
                queryFactory.select(card.id).from(card).where(card.listId.eq(listId)).fetch();

        queryFactory.delete(comment).where(comment.cardId.in(cardIds)).execute();

        queryFactory.delete(cardLog).where(cardLog.cardId.in(cardIds)).execute();

        queryFactory.delete(card).where(card.listId.eq(listId)).execute();

        QBoardList boardList = QBoardList.boardList;

        queryFactory.delete(boardList).where(boardList.id.eq(listId)).execute();
    }
}
