package com.sparta.springtrello.domain.workspace.repository;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springtrello.domain.board.entitiy.QBoard;
import com.sparta.springtrello.domain.card.entity.QCard;
import com.sparta.springtrello.domain.comment.entity.QComment;
import com.sparta.springtrello.domain.list.entity.QBoardList;
import com.sparta.springtrello.domain.user.entity.QUserWorkspace;
import com.sparta.springtrello.domain.workspace.entity.QWorkspace;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WorkspaceQueryRepositoryImpl implements WorkspaceQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    @Transactional
    public void deleteWorkspaceWithAllData(Long workspaceId) {
        QBoard board = QBoard.board;
        QBoardList list = QBoardList.boardList;
        QCard card = QCard.card;
        QComment comment = QComment.comment;
        QUserWorkspace userWorkspace = QUserWorkspace.userWorkspace;

        // Step 1: 워크스페이스 내의 모든 보드 ID 조회
        List<Long> boardIds =
                queryFactory
                        .select(board.id)
                        .from(board)
                        .where(board.workspaceId.eq(workspaceId))
                        .fetch();

        if (!boardIds.isEmpty()) {
            // Step 2: 보드와 연결된 리스트 ID 조회
            List<Long> listIds =
                    queryFactory
                            .select(list.id)
                            .from(list)
                            .where(list.boardId.in(boardIds))
                            .fetch();

            if (!listIds.isEmpty()) {
                // Step 3: 리스트와 연결된 카드 ID 조회
                List<Long> cardIds =
                        queryFactory
                                .select(card.id)
                                .from(card)
                                .where(card.listId.in(listIds))
                                .fetch();

                if (!cardIds.isEmpty()) {
                    // Step 4: 카드와 연결된 댓글 삭제

                    queryFactory.delete(comment).where(comment.cardId.in(cardIds)).execute();

                    // Step 5: 카드 삭제
                    queryFactory.delete(card).where(card.id.in(cardIds)).execute();
                }

                // Step 6: 리스트 삭제
                queryFactory.delete(list).where(list.id.in(listIds)).execute();
            }

            // Step 7: 보드 삭제
            queryFactory.delete(board).where(board.id.in(boardIds)).execute();
        }

        // Step 8: UserWorkspace에서 워크스페이스와 연관된 모든 사용자 관계 삭제
        queryFactory
                .delete(userWorkspace)
                .where(userWorkspace.workspaceId.eq(workspaceId))
                .execute();

        // Step 9: 워크스페이스 삭제
        queryFactory
                .delete(QWorkspace.workspace)
                .where(QWorkspace.workspace.id.eq(workspaceId))
                .execute();
    }
}
