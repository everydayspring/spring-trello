package com.sparta.springtrello.domain.search.specification;

import jakarta.persistence.criteria.*;

import org.springframework.data.jpa.domain.Specification;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.search.dto.CardSearchRequestDto;
import com.sparta.springtrello.domain.user.entity.User;

public class CardSpecification {
    public static Specification<Card> searchByCriteria(CardSearchRequestDto searchDto) {
        return (Root<Card> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            // 카드 검색
            if (searchDto.getName() != null && !searchDto.getName().isEmpty()) {
                predicate =
                        criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.like(
                                        root.get("name"), "%" + searchDto.getName() + "%"));
            }

            if (searchDto.getDescription() != null && !searchDto.getDescription().isEmpty()) {
                predicate =
                        criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.like(
                                        root.get("description"),
                                        "%" + searchDto.getDescription() + "%"));
            }

            if (searchDto.getDueDate() != null) {
                predicate =
                        criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.equal(root.get("dueDate"), searchDto.getDueDate()));
            }
            // 매니저 이메일 검색
            if (searchDto.getManagerEmail() != null && !searchDto.getManagerEmail().isEmpty()) {
                Subquery<Long> userSubquery = query.subquery(Long.class);
                Root<User> userRoot = userSubquery.from(User.class);
                userSubquery
                        .select(userRoot.get("id"))
                        .where(
                                criteriaBuilder.equal(
                                        userRoot.get("email"), searchDto.getManagerEmail()));

                predicate =
                        criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.in(root.get("managerId")).value(userSubquery));
            }

            if (searchDto.getBoardId() != null) {
                // listId로 lists 테이블을 조인한 다음 boardId를 검색
                Subquery<Long> listSubquery = query.subquery(Long.class);
                Root<BoardList> listRoot = listSubquery.from(BoardList.class);
                listSubquery
                        .select(listRoot.get("id"))
                        .where(
                                criteriaBuilder.equal(
                                        listRoot.get("boardId"), searchDto.getBoardId()));

                predicate =
                        criteriaBuilder.and(
                                predicate,
                                criteriaBuilder.in(root.get("listId")).value(listSubquery));
            }

            return predicate;
        };
    }
}
