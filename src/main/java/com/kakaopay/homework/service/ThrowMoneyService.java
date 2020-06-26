package com.kakaopay.homework.service;

import com.kakaopay.homework.web.dto.GetMoneyResponseDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyResponseDTO;

public interface ThrowMoneyService {

    ThrowMoneyResponseDTO throwMoney(String userId, String chatRoomId, ThrowMoneyRequestDTO throwMoneyRequestDTO);

    GetMoneyResponseDTO getThrowMoney(String userId, String chatRoomId, String token);

}
