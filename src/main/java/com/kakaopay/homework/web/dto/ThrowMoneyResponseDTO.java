package com.kakaopay.homework.web.dto;

import lombok.Data;

@Data
public class ThrowMoneyResponseDTO {

    // 토큰
    private String token;

    // 뿌려진 금액중 최대 금액
    private long maxMoney;

    // 뿌린 사람
    private String throwUserId;

}
