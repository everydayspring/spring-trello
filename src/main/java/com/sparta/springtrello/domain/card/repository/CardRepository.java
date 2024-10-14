package com.sparta.springtrello.domain.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.card.entity.Card;

public interface CardRepository extends JpaRepository<Card, Long> {}
