package com.kakaopay.homework.web.dto;

import lombok.Data;

@Data
public class ThrowMoneyRequestDTO {

    // 뿌릴 금액
    private long totalMoney;

    // 뿌릴 인원
    private int throwPeople;

}
