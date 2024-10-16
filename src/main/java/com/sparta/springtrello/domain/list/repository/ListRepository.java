package com.sparta.springtrello.domain.list.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.list.entity.BoardList;

public interface ListRepository extends JpaRepository<BoardList, Long> {
    List<BoardList> findAllByBoardId(Long boardId);
}
