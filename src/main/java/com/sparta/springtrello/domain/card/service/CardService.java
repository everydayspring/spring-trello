package com.sparta.springtrello.domain.card.service;

import java.io.IOException;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.springtrello.domain.board.entitiy.Board;
import com.sparta.springtrello.domain.board.repository.BoardRepository;
import com.sparta.springtrello.domain.card.dto.CreateCardDto;
import com.sparta.springtrello.domain.card.dto.GetCardDto;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.entity.CardLog;
import com.sparta.springtrello.domain.card.repository.CardLogRepository;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.comment.entity.Comment;
import com.sparta.springtrello.domain.comment.repository.CommentRepository;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.common.service.FileUploadService;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
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
    private final CardLogRepository cardLogRepository;
    private final CommentRepository commentRepository;
    private final FileUploadService fileUploadService;

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String CARD_VIEW_COUNT_KEY_PREFIX = "card:viewCount:";
    private static final String CARD_VIEW_USERS_KEY_PREFIX = "card:viewUsers:";

    // 카드 생성
    @Transactional
    public Card createCard(AuthUser authUser, CreateCardDto.Request request, MultipartFile file)
            throws IOException {

        BoardList boardList =
                listRepository
                        .findById(request.getListId())
                        .orElseThrow(() -> new InvalidRequestException("List not found"));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(() -> new InvalidRequestException("Board not found"));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(() -> new InvalidRequestException("워크스페이스에 대한 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new InvalidRequestException("READ ONLY 유저는 카드를 생성할 수 없습니다.");
        }

        if (userRepository.findById(request.getManagerId()).isEmpty()) {
            throw new InvalidRequestException("Manager not found");
        }

        UserWorkspace managerWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(request.getManagerId(), board.getWorkspaceId())
                        .orElseThrow(() -> new InvalidRequestException("담당자가 워크스페이스에 없습니다."));

        if (managerWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new InvalidRequestException("READ ONLY 유저는 담당자로 등록할 수 없습니다.");
        }

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
        cardLogRepository.save(new CardLog(card));

        return card;
    }

    // 카드 디건 조회
    public List<Card> getCards(Long listId, AuthUser authUser) {

        BoardList boardList =
                listRepository
                        .findById(listId)
                        .orElseThrow(() -> new InvalidRequestException("해당 리스트를 찾을 수 없습니다."));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(
                                () -> new InvalidRequestException("해당 리스트가 속한 보드를 찾을 수 없습니다."));

        if (userWorkspaceRepository
                .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                .isEmpty()) {
            throw new InvalidRequestException("해당 워크스페이스에 대한 접근 권한이 없습니다.");
        }

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
                        .orElseThrow(() -> new InvalidRequestException("Card not found"));

        BoardList boardList =
                listRepository
                        .findById(card.getListId())
                        .orElseThrow(() -> new InvalidRequestException("List not found"));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(() -> new InvalidRequestException("Board not found"));

        UserWorkspace userWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                        .orElseThrow(() -> new InvalidRequestException("워크스페이스에 대한 권한이 없습니다."));

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new InvalidRequestException("READ ONLY 유저는 카드를 수정할 수 없습니다.");
        }

        Long sequence = card.getSequence();

        if (!card.getListId().equals(request.getListId())) {
            BoardList newList =
                    listRepository
                            .findById(request.getListId())
                            .orElseThrow(() -> new InvalidRequestException("List not found"));

            Board newBoard =
                    boardRepository
                            .findById(newList.getBoardId())
                            .orElseThrow(() -> new InvalidRequestException("Board not found"));

            if (!board.getWorkspaceId().equals(newBoard.getWorkspaceId())) {
                throw new InvalidRequestException("다른 워크스페이스로는 변경할 수 없습니다");
            }

            sequence = cardRepository.countByListId(newList.getId());
            sequence++;
        }

        if (userRepository.findById(request.getManagerId()).isEmpty()) {
            throw new InvalidRequestException("Manager not found");
        }

        UserWorkspace managerWorkspace =
                userWorkspaceRepository
                        .findByUserIdAndWorkspaceId(request.getManagerId(), board.getWorkspaceId())
                        .orElseThrow(() -> new InvalidRequestException("담당자가 워크스페이스에 없습니다."));

        if (managerWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new InvalidRequestException("READ ONLY 유저는 담당자로 등록할 수 없습니다.");
        }

        card.updateCard(
                request.getName(),
                sequence,
                request.getDescription(),
                request.getDueDate(),
                request.getManagerId(),
                request.getListId());

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileUploadService.uploadFile(file);

            card.addFile(file.getOriginalFilename(), fileUrl);
        }

        cardLogRepository.save(new CardLog(card));

        return card;
    }

    // 카드 삭제
    @Transactional
    public void deleteCard(Long cardId, AuthUser authUser) {

        Card card =
                cardRepository
                        .findById(cardId)
                        .orElseThrow(() -> new InvalidRequestException("해당 카드를 찾을 수 없습니다."));

        BoardList boardList =
                listRepository
                        .findById(card.getListId())
                        .orElseThrow(
                                () -> new InvalidRequestException("해당 카드가 속한 리스트를 찾을 수 없습니다."));

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

        if (userWorkspace.getWorkspaceUserRole() == WorkspaceUserRole.READ_ONLY) {
            throw new InvalidRequestException("읽기 전용 권한을 가진 유저는 카드를 삭제할 수 없습니다.");
        }

        cardRepository.deleteCard(card.getId());

        List<Card> cards = cardRepository.findCardsReorder(card.getListId(), card.getSequence());

        for (Card targetCard : cards) {
            targetCard.changeSequence(targetCard.getSequence() - 1);
        }
    }

    public GetCardDto.Response getCard(Long id, AuthUser authUser) {
        Card card =
                cardRepository
                        .findById(id)
                        .orElseThrow(() -> new InvalidRequestException("Card not found"));

        BoardList boardList =
                listRepository
                        .findById(card.getListId())
                        .orElseThrow(() -> new InvalidRequestException("List not found"));

        Board board =
                boardRepository
                        .findById(boardList.getBoardId())
                        .orElseThrow(() -> new InvalidRequestException("Board not found"));

        if (userWorkspaceRepository
                .findByUserIdAndWorkspaceId(authUser.getId(), board.getWorkspaceId())
                .isEmpty()) {
            throw new InvalidRequestException("워크스페이스에 대한 권한이 없습니다.");
        }

        incrementCardViewCount(id, authUser.getId());

        List<CardLog> cardLogs = cardLogRepository.findByCardId(id);
        List<Comment> comments = commentRepository.findByCardId(id);

        return new GetCardDto.Response(card, cardLogs, comments);
    }

    public void incrementCardViewCount(Long cardId, Long userId) {
        String cardViewCountKey = CARD_VIEW_COUNT_KEY_PREFIX + cardId;
        String cardViewUsersKey = CARD_VIEW_USERS_KEY_PREFIX + cardId;

        if (!redisTemplate.opsForSet().isMember(cardViewUsersKey, userId)) {
            redisTemplate.opsForSet().add(cardViewUsersKey, userId);
            redisTemplate.opsForValue().increment(cardViewCountKey, 1);
        }
    }
}
