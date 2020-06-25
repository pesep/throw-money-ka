package com.kakaopay.homework.web;

import com.kakaopay.homework.service.ThrowMoneyService;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThrowMoneyController {

    @Autowired
    private ThrowMoneyService throwMoneyService;

    @PostMapping("/money/throw")
    public ResponseEntity<?> throwMoney(
            @RequestHeader(name = "X-USER-ID") String userId,
            @RequestHeader(name = "X-ROOM-ID") String chatRoomId,
            @RequestBody ThrowMoneyRequestDTO throwMoneyRequestDTO) {

        return ResponseEntity.ok(throwMoneyService.throwMoney(userId, chatRoomId, throwMoneyRequestDTO));

    }

}
