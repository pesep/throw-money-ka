package com.kakaopay.homework.service;

import com.kakaopay.homework.domain.money.MoneyDivision;
import com.kakaopay.homework.domain.money.MoneyDivisionRepository;
import com.kakaopay.homework.domain.money.ThrowMoneyDetail;
import com.kakaopay.homework.domain.money.ThrowMoneyDetailRepository;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class ThrowMoneyServiceImpl implements ThrowMoneyService {

    @Autowired
    private ThrowMoneyDetailRepository throwMoneyDetailRepository;

    @Autowired
    private MoneyDivisionRepository moneyDivisionRepository;


    @Override
    public ThrowMoneyResponseDTO throwMoney(String userId, String chatRoomId, ThrowMoneyRequestDTO throwMoneyRequestDTO) {

        String token = createToken();

        long divisionMoney = throwMoneyRequestDTO.getTotalMoney();
        int people = throwMoneyRequestDTO.getThrowPeople();

        ThrowMoneyDetail throwMoneyDetail = new ThrowMoneyDetail();
        throwMoneyDetail.setToken(token);
        throwMoneyDetail.setGroupChatId(chatRoomId);
        throwMoneyDetail.setMoneyMaker(userId);
        throwMoneyDetail.setThrowPeople(people);
        throwMoneyDetail.setTotalMoney(divisionMoney);

        throwMoneyDetailRepository.save(throwMoneyDetail);

        long[] divisionMoneyGroup = divisionMoney(people, divisionMoney);

        long max = divisionMoneyGroup[0];

        for (int i = 0; i < people; i++) {
            MoneyDivision moneyDivision = new MoneyDivision();
            moneyDivision.setMoneyThrowToken(token);
            moneyDivision.setDividedMoney(divisionMoneyGroup[i]);

            moneyDivisionRepository.save(moneyDivision);

            if (divisionMoneyGroup[i] > max) {
                max = divisionMoneyGroup[i];
            }

        }

        ThrowMoneyResponseDTO throwMoneyResponseDTO = new ThrowMoneyResponseDTO();

        throwMoneyResponseDTO.setMaxMoney(max);
        throwMoneyResponseDTO.setThrowUserId(userId);

        return throwMoneyResponseDTO;
    }

    private String createToken() {

        return RandomStringUtils.randomAlphanumeric(3);
    }

    private long[] divisionMoney(int people, long divisionMoney) {

        long[] divisionMoneyGroup = new long[people];

        for (int i = 0; i < people; i++) {

            if (i == people - 1) {
                divisionMoneyGroup[i] = divisionMoney;
                break;
            }

            divisionMoneyGroup[i] = ThreadLocalRandom.current().nextLong(1, divisionMoney);
            divisionMoney -= divisionMoneyGroup[i];

        }

        return divisionMoneyGroup;
    }

}
