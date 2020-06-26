package com.kakaopay.homework.service;

import com.kakaopay.homework.web.dto.GetMoneyResponseDTO;
import com.kakaopay.homework.web.dto.GetThrowMoneyDetailResponseDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyResponseDTO;

public interface ThrowMoneyService {

    // 머니 뿌리기
    ThrowMoneyResponseDTO throwMoney(String userId, String chatRoomId, ThrowMoneyRequestDTO throwMoneyRequestDTO);

    // 머니 받기
    GetMoneyResponseDTO getThrowMoney(String userId, String chatRoomId, String token);

    // 머니 뿌리기 조회
    GetThrowMoneyDetailResponseDTO getThrowMoneyDetail(String userId, String chatRoomId, String token);

}
