package com.sparta.springtrello.domain.list.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.list.entity.List;

public interface ListRepository extends JpaRepository<List, Long> {}
