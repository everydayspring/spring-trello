package com.sparta.springtrello.domain.auth.service;

import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.list.entity.List;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.Workspace;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final WorkspaceRepository workspaceRepository;
    private final BoardRepository boardRepository;
    private final ListRepository listRepository;
    private final CardRepository cardRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public void test() {
        userRepository.save(
                new User(
                        "user1@mail.com", passwordEncoder.encode("abc123?!"), UserRole.ROLE_ADMIN));
        userRepository.save(
                new User(
                        "user2@mail.com", passwordEncoder.encode("abc123?!"), UserRole.ROLE_ADMIN));
        userRepository.save(
                new User("user3@mail.com", passwordEncoder.encode("abc123?!"), UserRole.ROLE_USER));
        userRepository.save(
                new User("user4@mail.com", passwordEncoder.encode("abc123?!"), UserRole.ROLE_USER));
        userRepository.save(
                new User("user5@mail.com", passwordEncoder.encode("abc123?!"), UserRole.ROLE_USER));

        workspaceRepository.save(new Workspace("workspace1", "description1"));
        workspaceRepository.save(new Workspace("workspace2", "description2"));
        workspaceRepository.save(new Workspace("workspace3", "description3"));

        boardRepository.save(new Board("board1", "description1", 1L));
        boardRepository.save(new Board("board2", "description2", 2L));
        boardRepository.save(new Board("board3", "description3", 3L));

        listRepository.save(new List("list1", 1L, 1L));
        listRepository.save(new List("list2", 2L, 1L));
        listRepository.save(new List("list3", 1L, 2L));
        listRepository.save(new List("list4", 2L, 2L));

        cardRepository.save(new Card("card1", "description1", LocalDateTime.now(), 1L, 1L));
        cardRepository.save(new Card("card2", "description2", LocalDateTime.now(), 2L, 1L));
        cardRepository.save(new Card("card3", "description3", LocalDateTime.now(), 3L, 2L));
        cardRepository.save(new Card("card4", "description4", LocalDateTime.now(), 4L, 2L));

        commentRepository.save(new Comment("emoji1", "content", 1L, 1L));
        commentRepository.save(new Comment("emoji2", "content", 2L, 1L));
        commentRepository.save(new Comment("emoji3", "content", 3L, 1L));
        commentRepository.save(new Comment("emoji4", "content", 4L, 1L));
    }
}
