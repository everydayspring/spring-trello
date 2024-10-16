package com.sparta.springtrello.domain.card.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.dto.CardRequestDto;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import com.sparta.springtrello.domain.user.entity.UserWorkspace;
import com.sparta.springtrello.domain.user.enums.WorkspaceUserRole;
import com.sparta.springtrello.domain.user.repository.UserWorkspaceRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final UserWorkspaceRepository userWorkspaceRepository;

    // 카드 생성
    @Transactional
    public Card createCard(CardRequestDto cardRequestDto, AuthUser authUser) {
        // 리스트 존재 여부 확인
        BoardList boardList =
                listRepository
                        .findById(cardRequestDto.getListId())
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

        // 카드 생성
        Card newCard =
                new Card(
                        cardRequestDto.getName(),
                        null,
                        cardRequestDto.getDescription(),
                        cardRequestDto.getDueDate(),
                        cardRequestDto.getManagerId(),
                        cardRequestDto.getListId());

        Long currentCardCount = cardRepository.countByListId(cardRequestDto.getListId());
        newCard.setSequence(currentCardCount + 1);

        return newCard;
    }

    // 카드 추가
    @Transactional
    public Card addCard(Long listId, CardRequestDto cardRequestDto, AuthUser authUser) {
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

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 카드를 생성할 수 없습니다.");
        }

        //
        Card newCard = new Card();
        newCard.setName(cardRequestDto.getName());
        newCard.setDescription(cardRequestDto.getDescription());
        newCard.setDueDate(cardRequestDto.getDueDate());
        newCard.setManagerId(cardRequestDto.getManagerId()); // 이 부분 확인
        newCard.setListId(listId);
        newCard.setSequence(cardRepository.countByListId(listId) + 1);

        return cardRepository.save(newCard);
    }

    // 카드 조회
    public List<Card> findAllByListId(Long listId, AuthUser authUser) {
        // 리스트 존재 여부 확인
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

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 카드를 생성할 수 없습니다.");
        }

        // 리스트에 속한 모든 카드 조회
        return cardRepository.findAllByListId(listId);
    }

    // 카드 수정
    @Transactional
    public Card updateCard(Long cardId, AuthUser authUser, CardRequestDto cardRequestDto) {

        Card card =
                cardRepository
                        .findById(cardId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));
        BoardList boardList =
                listRepository
                        .findById(cardRequestDto.getListId())
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

        // 카드 정보 수정
        card.updateCard(
                cardRequestDto.getName(),
                cardRequestDto.getDescription(),
                cardRequestDto.getDueDate(),
                cardRequestDto.getManagerId());

        // 리스트 ID 변경 여부 확인
        Long newListId = cardRequestDto.getListId();
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
    }

    @Transactional
    public void changeCardOrder(
            Long cardId, Long targetListId, Long newPosition, AuthUser authUser) {
        // 카드 존재 여부 확인
        Card card =
                cardRepository
                        .findById(cardId)
                        .orElseThrow(() -> new IllegalArgumentException("해당 카드를 찾을 수 없습니다."));

        // 이동할 리스트 존재 여부 확인
        BoardList targetList =
                listRepository
                        .findById(targetListId)
                        .orElseThrow(() -> new IllegalArgumentException("이동할 리스트를 찾을 수 없습니다."));

        // 리스트가 속한 보드 존재 여부 확인
        Board board =
                boardRepository
                        .findById(targetList.getBoardId())
                        .orElseThrow(() -> new IllegalArgumentException("리스트가 속한 보드를 찾을 수 없습니다."));

        // 사용자의 워크스페이스 권한 확인
        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(
                                () -> new IllegalArgumentException("해당 워크스페이스에 대한 접근 권한이 없습니다."));

        // 읽기 전용 권한 확인
        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 권한을 가진 유저는 카드를 이동할 수 없습니다.");
        }

        // 현재 카드의 순서와 새로운 위치를 비교
        Long currentOrder = card.getSequence();

        // 같은 리스트에서 순서 변경
        if (targetListId.equals(card.getListId())) {
            // 같은 리스트 내에서 이동하는 경우
            if (newPosition > currentOrder) {
                // 순서를 감소시킬 카드들 업데이트
                cardRepository.findAllByListId(targetListId).stream()
                        .filter(
                                c ->
                                        c.getSequence() > currentOrder
                                                && c.getSequence() <= newPosition)
                        .forEach(
                                c -> {
                                    c.setSequence(c.getSequence() - 1);
                                    cardRepository.save(c); // 업데이트된 카드 저장
                                });
            } else {
                // 순서를 증가시킬 카드들 업데이트
                cardRepository.findAllByListId(targetListId).stream()
                        .filter(
                                c ->
                                        c.getSequence() < currentOrder
                                                && c.getSequence() >= newPosition)
                        .forEach(
                                c -> {
                                    c.setSequence(c.getSequence() + 1);
                                    cardRepository.save(c); // 업데이트된 카드 저장
                                });
            }
        } else {
            // 다른 리스트로 이동하는 경우
            // 현재 리스트에서 카드 삭제 및 새로운 리스트로 추가
            List<Card> cardsInTargetList = cardRepository.findAllByListId(targetListId);

            // 새로운 위치 이후의 카드들 순서 증가
            cardsInTargetList.stream()
                    .filter(c -> c.getSequence() >= newPosition)
                    .forEach(
                            c -> {
                                c.setSequence(c.getSequence() + 1);
                                cardRepository.save(c); // 업데이트된 카드 저장
                            });
        }

        // 카드의 리스트와 순서 업데이트
        card.setListId(targetListId);
        card.setSequence(newPosition);
        cardRepository.save(card);
    }
}
