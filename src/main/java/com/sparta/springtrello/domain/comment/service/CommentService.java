package com.sparta.springtrello.domain.comment.service;

import java.util.NoSuchElementException;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.dto.request.CommentRequest;
import com.sparta.springtrello.domain.comment.dto.request.CommentUpdateRequest;
import com.sparta.springtrello.domain.comment.dto.response.CommentResponse;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.entity.UserWorkspace;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final WorkspaceRepository workspaceRepository;
    private final CardRepository cardRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    public CommentResponse saveComment(AuthUser authUser, CommentRequest request) {
        User user = User.fromAuthUser(authUser);
        validateInputs(request.getWorkspaceId(), request.getCardId());

        UserWorkspace userWorkspace = getUserWorkspace(authUser.getId(), request.getWorkspaceId());
        checkUserWorkspacePermission(userWorkspace);

        Comment comment = createComment(request, user);
        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.entityToDto(savedComment);
    }

    public CommentResponse updateComment(Long id, AuthUser authUser, CommentUpdateRequest request) {
        User user = User.fromAuthUser(authUser);
        Comment comment = getCommentById(id);

        checkCommentOwnership(comment, user);
        comment.update(request.getContent(), request.getEmoji());

        commentRepository.save(comment);
        return CommentResponse.entityToDto(comment);
    }

    public void deleteComment(Long id, AuthUser authUser) {
        User user = User.fromAuthUser(authUser);
        Comment comment = getCommentById(id);

        checkCommentOwnership(comment, user);
        commentRepository.delete(comment);
    }

    private void validateInputs(Long workspaceId, Long cardId) {
        validateWorkspaceId(workspaceId);
        validateCardId(cardId);
    }

    private void validateWorkspaceId(Long workspaceId) {
        if (workspaceId == null) {
            throw new NoSuchElementException("워크스페이스 아이디는 필수입니다.");
        }
        if (!workspaceRepository.existsById(workspaceId)) {
            throw new EntityNotFoundException("존재하지 않는 워크스페이스 아이디입니다.");
        }
    }

    private void validateCardId(Long cardId) {
        if (cardId == null) {
            throw new NoSuchElementException("카드 아이디는 필수입니다.");
        }
        if (!cardRepository.existsById(cardId)) {
            throw new EntityNotFoundException("존재하지 않는 카드 아이디입니다.");
        }
    }

    private UserWorkspace getUserWorkspace(Long userId, Long workspaceId) {
        return userWorkspaceRepository
                .findByUserIdAndWorkspaceId(userId, workspaceId)
                .orElseThrow(() -> new InvalidRequestException("워크스페이스 멤버가 아닙니다"));
    }

    private void checkUserWorkspacePermission(UserWorkspace userWorkspace) {
        if (userWorkspace.getWorkspaceUserRole().equals(WorkspaceUserRole.READ_ONLY)) {
            throw new InvalidRequestException("읽기 전용 역할을 가진 사용자는 댓글을 작성할 수 없습니다.");
        }
    }

    private Comment createComment(CommentRequest request, User user) {
        return new Comment(
                request.getEmoji(),
                request.getContent(),
                request.getCardId(),
                request.getWorkspaceId(),
                user.getId());
    }

    private Comment getCommentById(Long id) {
        return commentRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException("댓글을 찾을 수 없습니다."));
    }

    private void checkCommentOwnership(Comment comment, User user) {
        if (!comment.getUserId().equals(user.getId())) {
            throw new AccessDeniedException("댓글을 수정할 권한이 없습니다.");
        }
    }
}
