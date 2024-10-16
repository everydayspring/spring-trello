package com.sparta.springtrello.domain.search.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.springtrello.domain.card.entity.Card;
import com.sparta.springtrello.domain.card.repository.CardRepository;
import com.sparta.springtrello.domain.search.dto.CardSearchRequestDto;
import com.sparta.springtrello.domain.search.specification.CardSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CardSearchService {
    private final CardRepository cardRepository;

    @Transactional(readOnly = true)
    public Page<Card> searchCards(CardSearchRequestDto searchDto, Pageable pageable) {
        Specification<Card> spec = CardSpecification.searchByCriteria(searchDto);
        return cardRepository.findAll(spec, pageable);
    }
}
