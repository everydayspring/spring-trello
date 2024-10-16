package com.sparta.springtrello.domain.comment.service;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.request.CommentUpdateRequest;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponse;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.entity.UserWorkspace;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;
    private final BoardRepository boardRepository;
    private final ListRepository listRepository;

    public CommentResponse saveComment(AuthUser authUser, CommentRequest request) {
        User user = User.fromAuthUser(authUser);

        Card card =
                cardRepository
                        .findById(request.getCardId())
                        .orElseThrow(() -> new InvalidRequestException("해당 카드를 찾을 수 없습니다."));

        BoardList boardList =
                listRepository
                        .findById(card.getListId())
                        .orElseThrow(() -> new InvalidRequestException("해당 리스트를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(
                                () -> new InvalidRequestException("해당 리스트가 속한 보드를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new InvalidRequestException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole().equals(WorkspaceUserRole.READ_ONLY)) {
            throw new InvalidRequestException("권한이 없습니다.");
        }

        Comment comment =
                new Comment(
                        request.getEmoji(),
                        request.getContent(),
                        request.getCardId(),
                        user.getId());

        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.entityToDto(savedComment);
    }

    public CommentResponse updateComment(Long id, AuthUser authUser, CommentUpdateRequest request) {
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
