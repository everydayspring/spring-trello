package com.sparta.springtrello.domain.card.service;

import com.sparta.springtrello.domain.card.controller.CardController;
import com.sparta.springtrello.domain.card.dto.CardRequestDto;
import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.list.dto.request.ListRequestDto;
import com.sparta.springtrello.domain.list.entity.BoardList;
import com.sparta.springtrello.domain.list.repository.ListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.card.repository.CardRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CardService {

    private final CardRepository cardRepository;
    private final ListRepository listRepository;

    //카드 생성
    public Card createCard(AuthUser authUser, CardRequestDto cardRequestDto){
        // 리스트 존재 여부 확인
        BoardList boardList = listRepository.findById(cardRequestDto.getListId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트를 찾을 수 없습니다."));
        // 카드 생성
        Card card = new Card(
                cardRequestDto.getName(),
                cardRequestDto.getSequence(),
                cardRequestDto.getDescription(),
                cardRequestDto.getDueDate(),
                cardRequestDto.getManagerId(),
                cardRequestDto.getListId()
        );
        return cardRepository.save(card);
    }

    //카드 조회

    //카드 수정

    //카드 삭제
}
