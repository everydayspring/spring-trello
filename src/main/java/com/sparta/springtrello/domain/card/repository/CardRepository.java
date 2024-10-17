package com.sparta.springtrello.domain.card.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sparta.springtrello.domain.card.entity.Card;

public interface CardRepository
        extends JpaRepository<Card, Long>, JpaSpecificationExecutor<Card>, CardQueryRepository {
    List<Card> findAllByListId(Long listId);

    long countByListId(Long listId);
}
