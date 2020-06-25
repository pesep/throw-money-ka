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
public class MoneyDivisionRepositoryTest {

    @Autowired
    MoneyDivisionRepository moneyDivisionRepository;

    @After
    public void cleanUp() {
        moneyDivisionRepository.deleteAll();
    }

    @Test
    public void getMoneyDivisionRepository() {

        MoneyDivision moneyDivision = new MoneyDivision();

        String moneyThrowToken = RandomStringUtils.randomAlphanumeric(3);
        long dividedMoney = 39;
        String receivedMoneyUserId = "steve";

        moneyDivision.setMoneyThrowToken(moneyThrowToken);
        moneyDivision.setDividedMoney(dividedMoney);
        moneyDivision.setReceivedMoneyUserId(receivedMoneyUserId);

        moneyDivisionRepository.save(moneyDivision);

        List<MoneyDivision> moneyDivisionList = moneyDivisionRepository.findAll();
        MoneyDivision data = moneyDivisionList.get(0);

        assertThat(data.getMoneyThrowToken()).isEqualTo(moneyThrowToken);
        assertThat(data.getDividedMoney()).isEqualTo(dividedMoney);
        assertThat(data.getReceivedMoneyUserId()).isEqualTo(receivedMoneyUserId);

    }
}
