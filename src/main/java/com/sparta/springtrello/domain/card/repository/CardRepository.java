package com.sparta.springtrello.domain.card.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByListId(Long listId);

    long countByListId(Long listId);
}
