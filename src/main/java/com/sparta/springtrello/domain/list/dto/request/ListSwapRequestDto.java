package com.sparta.springtrello.domain.list.dto.request;

import lombok.Getter;

@Getter
public class ListSwapRequestDto {
    private final Long list1;
    private final Long list2;

    public ListSwapRequestDto(Long list1, Long list2) {
        this.list1 = list1;
        this.list2 = list2;
    }
}
