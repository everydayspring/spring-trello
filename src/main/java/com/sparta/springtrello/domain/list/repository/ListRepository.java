package com.sparta.springtrello.domain.list.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.list.entity.BoardList;

public interface ListRepository extends JpaRepository<BoardList, Long> {
    Long countByBoardId(Long id);
}
