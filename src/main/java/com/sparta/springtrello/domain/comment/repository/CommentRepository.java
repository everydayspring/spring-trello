package com.sparta.springtrello.domain.comment.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.springtrello.domain.comment.entity.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}
