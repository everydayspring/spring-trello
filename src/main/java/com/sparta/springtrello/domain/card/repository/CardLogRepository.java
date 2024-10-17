package com.sparta.springtrello.domain.card.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.card.entity.CardLog;

public interface CardLogRepository extends JpaRepository<CardLog, Long> {
    List<CardLog> findByCardId(Long cardId);
}
