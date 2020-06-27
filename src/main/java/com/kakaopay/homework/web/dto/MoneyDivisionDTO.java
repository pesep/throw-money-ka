package com.kakaopay.homework.web.dto;

import lombok.Data;

@Data
public class MoneyDivisionDTO {

    // 받은 금액
    private long receivedMoney;

    // 받은 사람
    private String receivedUserId;

}
