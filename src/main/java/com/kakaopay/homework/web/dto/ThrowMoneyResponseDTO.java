package com.kakaopay.homework.web.dto;

import lombok.Data;

@Data
public class ThrowMoneyResponseDTO {

    private String token;

    private long maxMoney;

    private String throwUserId;

}
