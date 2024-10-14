package com.sparta.springtrello.domain.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.board.entitiy.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {}
