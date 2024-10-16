package com.sparta.springtrello.domain.card.repository;

import java.util.List;

import com.sparta.springtrello.domain.comment.entity.QComment;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.entity.QCard;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CardQueryRepositoryImpl implements CardQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Card> findCardsReorder(Long listId, Long sequence) {
        QCard card = QCard.card;

        return queryFactory
                .selectFrom(card)
                .where(
                        card.listId
                                .eq(listId)
                                .and(card.sequence.gt(sequence))) // 삭제된 카드의 순서보다 큰 카드들만 조회
                .orderBy(card.sequence.asc()) // 기존 순서대로 정렬
                .fetch();
    }

    @Override
    public void deleteCard(Long cardId) {
        QComment comment = QComment.comment;
        QCard card = QCard.card;

        queryFactory
                .delete(comment)
                .where(comment.cardId.eq(cardId))
                .execute();

        queryFactory
                .delete(card)
                .where(card.id.eq(cardId))
                .execute();
    }
}
