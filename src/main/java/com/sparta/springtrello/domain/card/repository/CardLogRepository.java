package com.sparta.springtrello.domain.card.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.card.entity.CardLog;

public interface CardLogRepository extends JpaRepository<CardLog, Long> {}
