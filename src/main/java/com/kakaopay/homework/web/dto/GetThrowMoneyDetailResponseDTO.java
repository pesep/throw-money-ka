package com.kakaopay.homework.web.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GetThrowMoneyDetailResponseDTO {

    private LocalDateTime throwDateTime;

    private long throwMoney;

    private long receivedMoney;

    private List<MoneyDivisionDTO> moneyDivisionDTOList;


}
