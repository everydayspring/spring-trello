package com.sparta.springtrello.domain.card.repository;

import java.util.List;

import com.sparta.springtrello.domain.card.entity.Card;

public interface CardQueryRepository {
    List<Card> findCardsReorder(Long listId, Long sequence);

    void deleteCard(Long cardId);
}
