package com.sparta.springtrello.domain.list.repository;

import java.util.List;

import com.sparta.springtrello.domain.list.entity.BoardList;

public interface ListQueryRepository {
    void deleteListWithCards(Long listId);

    List<BoardList> findListsByBoardId(Long boardId);

    List<BoardList> findListsReorder(Long boardId, Long sequence);
}
