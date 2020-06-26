package com.kakaopay.homework.web;

import com.kakaopay.homework.service.ThrowMoneyService;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/money/get/{token}")
    public ResponseEntity<?> getMoney(
            @RequestHeader(name = "X-USER-ID") String userId,
            @RequestHeader(name = "X-ROOM-ID") String chatRoomId,
            @PathVariable String token) {

        return ResponseEntity.ok(throwMoneyService.getThrowMoney(userId, chatRoomId, token));

    }

    @GetMapping("/money/get/{token}")
    public ResponseEntity<?> getThrowMoneyDetail(
            @RequestHeader(name = "X-USER-ID") String userId,
            @RequestHeader(name = "X-ROOM-ID") String chatRoomId,
            @PathVariable String token) {

        return ResponseEntity.ok(throwMoneyService.getThrowMoneyDetail(userId, chatRoomId, token));
    }

}
