package com.kakaopay.homework.service;

import com.kakaopay.homework.domain.money.ThrowMoneyDetail;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;

public interface ThrowMoneyService {

    ThrowMoneyDetail throwMoney(String userId, String chatRoomId, ThrowMoneyRequestDTO throwMoneyRequestDTO);

}
