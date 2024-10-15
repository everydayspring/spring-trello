package com.sparta.springtrello.domain.comment.service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.comment.dto.request.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponse;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentResponse saveComment(AuthUser authUser, CommentRequest request) {
        User user = User.fromAuthUser(authUser);

        Comment comment =
                new Comment(
                        request.getEmoji(),
                        request.getContent(),
                        request.getCardId(),
                        user.getId());

        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.entityToDto(savedComment);
    }

    public CommentResponse updateComment(Long id, AuthUser authUser, CommentRequest request) {
        User user = User.fromAuthUser(authUser);
        Comment comment =
                commentRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("댓글을 수정할 권한이 없습니다.");
        }

        comment.update(request.getContent(), request.getEmoji());

        commentRepository.save(comment);

        return CommentResponse.entityToDto(comment);
    }

    public void deleteComment(Long id, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Comment comment =
                commentRepository
                        .findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("댓글을 삭제할 권한이 없습니다.");
        }

        commentRepository.delete(comment);
    }
}
