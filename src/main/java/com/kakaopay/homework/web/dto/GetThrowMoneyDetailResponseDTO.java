package com.kakaopay.homework.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetThrowMoneyDetailResponseDTO {

    // 뿌린 시간
    private LocalDateTime throwDateTime;

    // 뿌린 금액
    private long throwMoney;

    // 받기 완료된 금액
    private long receivedMoney;

    // 받기 완료된 정보
    private List<MoneyDivisionDTO> moneyDivisionDTOList;


}
