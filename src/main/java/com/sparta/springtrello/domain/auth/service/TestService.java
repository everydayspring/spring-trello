package com.sparta.springtrello.domain.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.entity.UserWorkspace;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;
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
    private final UserWorkspaceRepository userWorkspaceRepository;

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

        workspaceRepository.save(new Workspace(1L, "workspace1", "description1"));
        workspaceRepository.save(new Workspace(2L, "workspace2", "description2"));
        workspaceRepository.save(new Workspace(3L, "workspace3", "description3"));
        workspaceRepository.save(new Workspace(4L, "workspace4", "description4"));
        workspaceRepository.save(new Workspace(5L, "workspace5", "description5"));

        boardRepository.save(new Board("board1", "description1", 1L));
        boardRepository.save(new Board("board2", "description2", 2L));
        boardRepository.save(new Board("board3", "description3", 3L));

        listRepository.save(new BoardList("list1", 1L, 1L));
        listRepository.save(new BoardList("list2", 2L, 1L));
        listRepository.save(new BoardList("list3", 1L, 2L));
        listRepository.save(new BoardList("list4", 2L, 2L));

        //        cardRepository.save(new Card("card1", 1L, "description1", LocalDateTime.now(), 1L,
        // 1L));
        //        cardRepository.save(new Card("card2", 2L, "description2", LocalDateTime.now(), 2L,
        // 1L));
        //        cardRepository.save(new Card("card3", 3L, "description3", LocalDateTime.now(), 3L,
        // 2L));
        //        cardRepository.save(new Card("card4", 4L, "description4", LocalDateTime.now(), 4L,
        // 2L));

        commentRepository.save(new Comment("emoji1", "content", 1L, 1L, 1L));
        commentRepository.save(new Comment("emoji2", "content", 2L, 1L, 1L));
        commentRepository.save(new Comment("emoji3", "content", 3L, 4L, 1L));
        commentRepository.save(new Comment("emoji4", "content", 4L, 5L, 1L));

        userWorkspaceRepository.save(new UserWorkspace(1L, 1L, WorkspaceUserRole.WORKSPACE));
        userWorkspaceRepository.save(new UserWorkspace(1L, 2L, WorkspaceUserRole.WORKSPACE));
        userWorkspaceRepository.save(new UserWorkspace(1L, 3L, WorkspaceUserRole.WORKSPACE));
        userWorkspaceRepository.save(new UserWorkspace(1L, 4L, WorkspaceUserRole.BOARD));
        userWorkspaceRepository.save(new UserWorkspace(1L, 5L, WorkspaceUserRole.READ_ONLY));
    }
}
