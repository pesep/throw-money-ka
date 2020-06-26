package com.kakaopay.homework.service;

import com.kakaopay.homework.domain.money.MoneyDivision;
import com.kakaopay.homework.domain.money.MoneyDivisionRepository;
import com.kakaopay.homework.domain.money.ThrowMoneyDetail;
import com.kakaopay.homework.domain.money.ThrowMoneyDetailRepository;
import com.kakaopay.homework.domain.user.ThrowUser;
import com.kakaopay.homework.domain.user.ThrowUserRepository;
import com.kakaopay.homework.exception.ErrorCode;
import com.kakaopay.homework.exception.ErrorCodeException;
import com.kakaopay.homework.web.dto.GetMoneyResponseDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
public class ThrowMoneyServiceImpl implements ThrowMoneyService {

    @Autowired
    private ThrowMoneyDetailRepository throwMoneyDetailRepository;

    @Autowired
    private MoneyDivisionRepository moneyDivisionRepository;

    @Autowired
    private ThrowUserRepository throwUserRepository;

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

        List<MoneyDivision> moneyDivisionList = new ArrayList<>();

        long[] divisionMoneyGroup = divisionMoney(people, divisionMoney);
        Collections.shuffle(Arrays.asList(divisionMoneyGroup));

        long max = divisionMoneyGroup[0];

        for (int i = 0; i < people; i++) {
            MoneyDivision moneyDivision = new MoneyDivision();
            moneyDivision.setToken(token);
            moneyDivision.setDividedMoney(divisionMoneyGroup[i]);
            moneyDivision.setThrowMoneyDetail(throwMoneyDetail);

            moneyDivisionList.add(moneyDivision);

            if (divisionMoneyGroup[i] > max) {
                max = divisionMoneyGroup[i];
            }

        }

        throwMoneyDetail.setMoneyDivisionList(moneyDivisionList);

        throwMoneyDetailRepository.save(throwMoneyDetail);

        ThrowMoneyResponseDTO throwMoneyResponseDTO = new ThrowMoneyResponseDTO();

        throwMoneyResponseDTO.setToken(token);
        throwMoneyResponseDTO.setMaxMoney(max);
        throwMoneyResponseDTO.setThrowUserId(userId);

        return throwMoneyResponseDTO;
    }

    @Override
    public GetMoneyResponseDTO getThrowMoney(String userId, String chatRoomId, String token) {

        ThrowMoneyDetail throwMoneyDetail = throwMoneyDetailRepository.findByToken(token);

        // 10분 제한
        LocalDateTime expireDateTime = throwMoneyDetail.getCreatedDate().plusMinutes(10);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expireDateTime)) {
            throw new ErrorCodeException(ErrorCode.E0001);
        }

        // 대화방에 소속되어 있는지 확인
        ThrowUser throwUser = throwUserRepository.findByUserIdAndChatRoomId(userId, chatRoomId);
        if (throwUser == null) {
            throw new ErrorCodeException(ErrorCode.E0002);
        }

        // 뿌린 사람은 가져갈수 없음
        if (throwMoneyDetail.getMoneyMaker().equals(userId)) {
            throw new ErrorCodeException(ErrorCode.E0003);
        }

        // 한번 가져간 사람은 다시 가져갈수 없음
        MoneyDivision moneyDivision = new MoneyDivision();
        for (int i = 0; i < throwMoneyDetail.getMoneyDivisionList().size(); i++) {
            if (throwMoneyDetail.getMoneyDivisionList().get(i).getReceivedMoneyUserId() != null
                    && throwMoneyDetail.getMoneyDivisionList().get(i).getReceivedMoneyUserId().equals(userId)) {
                throw new ErrorCodeException(ErrorCode.E0004);
            }

            if (throwMoneyDetail.getMoneyDivisionList().get(i).getReceivedMoneyUserId() == null) {
                moneyDivision.setSeq(throwMoneyDetail.getMoneyDivisionList().get(i).getSeq());
                moneyDivision.setToken(throwMoneyDetail.getMoneyDivisionList().get(i).getToken());
                moneyDivision.setDividedMoney(throwMoneyDetail.getMoneyDivisionList().get(i).getDividedMoney());
                break;
            }

        }

        moneyDivision.setReceivedMoneyUserId(userId);
        moneyDivision.setThrowMoneyDetail(throwMoneyDetail);

        moneyDivisionRepository.save(moneyDivision);

        GetMoneyResponseDTO getMoneyResponseDTO = new GetMoneyResponseDTO();
        getMoneyResponseDTO.setMoney(moneyDivision.getDividedMoney());

        return getMoneyResponseDTO;
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
            if (divisionMoney == people - i) {
                divisionMoneyGroup[i] = 1;

            } else {
                divisionMoneyGroup[i] = ThreadLocalRandom.current().nextLong(1, divisionMoney - people + i + 1);

            }

            divisionMoney -= divisionMoneyGroup[i];

        }

        return divisionMoneyGroup;
    }

}
