package com.sparta.springtrello.domain.card.service;

import java.io.IOException;
import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.dto.CreateCardDto;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.entity.QCard;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.user.entity.User;
import com.sparta.springtrello.domain.user.entity.UserWorkspace;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    private final JPAQueryFactory queryFactory;

    private final FileUploadService fileUploadService;

    // 카드 생성
    @Transactional
    public Card createCard(AuthUser authUser, CreateCardDto.Request request, MultipartFile file)
            throws IOException {

        BoardList boardList =
                listRepository
                        .findById(request.getListId())
                        .orElseThrow(() -> new IllegalArgumentException("List not found"));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(() -> new IllegalArgumentException("Board not found"));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(() -> new IllegalArgumentException("워크스페이스에 대한 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("READ ONLY 유저는 카드를 생성할 수 없습니다.");
        }

        if (userRepository.findById(request.getManagerId()).isEmpty()) {
            throw new InvalidRequestException("Manager not found");
        }

        UserWorkspace userWorkspace2 =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(request.getManagerId(), board.getWorkspaceId())
                        .orElseThrow(() -> new IllegalArgumentException("담당자가 워크스페이스에 없습니다."));

        if (userWorkspace2.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("READ ONLY 유저는 담당자로 등록할 수 없습니다.");
        }

        // 리스트 내 순서 세팅
        Long sequence = cardRepository.countByListId(request.getListId());
        sequence++;

        Card card =
                new Card(
                        request.getName(),
                        sequence,
                        request.getDescription(),
                        request.getDueDate(),
                        request.getManagerId(),
                        request.getListId(),
                        null,
                        null);

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(file); // 파일 업로드 후 URL 반환

            card.addFile(file.getOriginalFilename(), fileUrl);
        }

        cardRepository.save(card);

        return card;
    }

    // 카드 조회
    public List<Card> findAllByListId(Long listId, AuthUser authUser) {

        BoardList boardList =
                listRepository
                        .findById(listId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 리스트가 속한 보드를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        // 리스트에 속한 모든 카드 조회
        return cardRepository.findAllByListId(listId);
    }

    // 카드 수정
    @Transactional
    public Card updateCard(
            Long cardId, AuthUser authUser, CreateCardDto.Request request, MultipartFile file)
            throws IOException {

        Card card =
                cardRepository
                        .findById(cardId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));
        BoardList boardList =
                listRepository
                        .findById(request.getListId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 리스트가 속한 보드를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 카드를 생성할 수 없습니다.");
        }
        User manager =
                userRepository
                        .findById(request.getManagerId())
                        .orElseThrow(() -> new IllegalArgumentException("매니저 ID가 유효하지 않습니다."));

        UserWorkspace userWorkspace2 =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(manager.getId(), board.getWorkspaceId())
                        .orElseThrow(() -> new IllegalArgumentException("담당자가 워크스페이스에 없습니다"));

        if (userWorkspace2.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("READ ONLY 유저는 담당자로 등록할 수 없습니다.");
        }

        String fileUrl = card.getFileUrl();
        if (file != null && !file.isEmpty()) {
            fileUrl = fileUploadService.uploadFile(file);
        }

        // 카드 정보 수정
        //        card.updateCard(
        //                cardRequestDto.getName(),
        //                cardRequestDto.getDescription(),
        //                cardRequestDto.getDueDate(),
        //                cardRequestDto.getManagerId(),
        //                file.getOriginalFilename(),
        //                fileUrl);
        //                manager.getId());

        // 리스트 ID 변경 여부 확인
        Long newListId = request.getListId();
        if (!card.getListId().equals(newListId)) {
            // 리스트가 변경된 경우
            card.setListId(newListId);

            // 새로운 리스트에 대한 시퀀스 재설정
            Long currentCardCount = cardRepository.countByListId(newListId);
            card.setSequence(currentCardCount + 1); // 새로운 시퀀스 설정
        } else {
            // 리스트가 변경되지 않은 경우, 기존 시퀀스 유지
            card.setListId(newListId);
        }

        return cardRepository.save(card);
    }

    // 카드 삭제
    @Transactional
    public void deleteCard(Long cardId, AuthUser authUser) {

        Card card =
                cardRepository
                        .findById(cardId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));
        BoardList boardList =
                listRepository
                        .findById(card.getListId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 카드가 속한 리스트를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 리스트가 속한 보드를 찾을 수 없습니다."));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 카드를 삭제할 수 없습니다.");
        }

        cardRepository.delete(card);

        List<Card> reorder =
                queryFactory
                        .selectFrom(QCard.card)
                        .where(
                                QCard.card
                                        .listId
                                        .eq(card.getListId())
                                        .and(
                                                QCard.card.sequence.gt(
                                                        card.getSequence()))) // 삭제된 카드의 순서보다 큰
                        // 카드들만 조회
                        .orderBy(QCard.card.sequence.asc()) // 기존 순서대로 정렬
                        .fetch();

        for (int i = 0; i < reorder.size(); i++) {
            Card remainingCard = reorder.get(i);
            remainingCard.setSequence(remainingCard.getSequence() - 1); // 순서를 1씩 줄임
            cardRepository.save(remainingCard); // 업데이트된 순서 저장
        }
    }
}
