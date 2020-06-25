package com.kakaopay.homework.service;

import com.kakaopay.homework.domain.money.MoneyDivisionRepository;
import com.kakaopay.homework.domain.money.ThrowMoneyDetail;
import com.kakaopay.homework.domain.money.ThrowMoneyDetailRepository;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class ThrowMoneyServiceImpl implements ThrowMoneyService {

    @Autowired
    private ThrowMoneyDetailRepository throwMoneyDetailRepository;

    @Autowired
    private MoneyDivisionRepository moneyDivisionRepository;


    @Override
    public ThrowMoneyDetail throwMoney(String userId, String chatRoomId, ThrowMoneyRequestDTO throwMoneyRequestDTO) {

        String token = createToken();

        long price = throwMoneyRequestDTO.getTotalMoney();

        int people = throwMoneyRequestDTO.getThrowPeople();

        long divisionMoney = ThreadLocalRandom.current().nextLong(1, price);


        return null;
    }

    private String createToken() {

        return RandomStringUtils.randomAlphanumeric(3);
    }

}
