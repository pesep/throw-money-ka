package com.kakaopay.homework.domain.money;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ThrowMoneyDetailRepositoryTest {

    @Autowired
    ThrowMoneyDetailRepository throwMoneyDetailRepository;

    @After
    public void cleanUp() {
        throwMoneyDetailRepository.deleteAll();
    }

    @Test
    public void getThrowMoneyDetailRepository() {

        ThrowMoneyDetail throwMoneyDetail = new ThrowMoneyDetail();

        String token = RandomStringUtils.randomAlphanumeric(3);
        String groupChatId = "test";
        String moneyMaker = "king";
        long totalMoney = 10000;
        int throwPeople = 5;

        throwMoneyDetail.setToken(token);
        throwMoneyDetail.setGroupChatId(groupChatId);
        throwMoneyDetail.setMoneyMaker(moneyMaker);
        throwMoneyDetail.setTotalMoney(totalMoney);
        throwMoneyDetail.setThrowPeople(throwPeople);

        throwMoneyDetailRepository.save(throwMoneyDetail);

        List<ThrowMoneyDetail> throwMoneyDetailList = throwMoneyDetailRepository.findAll();
        ThrowMoneyDetail data = throwMoneyDetailList.get(0);

        assertThat(data.getToken()).isEqualTo(token);
        assertThat(data.getGroupChatId()).isEqualTo(groupChatId);
        assertThat(data.getMoneyMaker()).isEqualTo(moneyMaker);
        assertThat(data.getTotalMoney()).isEqualTo(totalMoney);
        assertThat(data.getThrowPeople()).isEqualTo(throwPeople);

    }
}
