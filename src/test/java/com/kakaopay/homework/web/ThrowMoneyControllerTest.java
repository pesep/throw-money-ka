package com.kakaopay.homework.web;

import com.kakaopay.homework.domain.money.MoneyDivision;
import com.kakaopay.homework.domain.money.MoneyDivisionRepository;
import com.kakaopay.homework.domain.money.ThrowMoneyDetailRepository;
import com.kakaopay.homework.service.ThrowMoneyService;
import com.kakaopay.homework.web.dto.ThrowMoneyRequestDTO;
import com.kakaopay.homework.web.dto.ThrowMoneyResponseDTO;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ThrowMoneyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ThrowMoneyService throwMoneyService;

    @Autowired
    private ThrowMoneyDetailRepository throwMoneyDetailRepository;

    @Autowired
    private MoneyDivisionRepository moneyDivisionRepository;

    @After
    public void tearDown() throws Exception {
        throwMoneyDetailRepository.deleteAll();
        moneyDivisionRepository.deleteAll();
    }

    @Test
    public void throwMoney() {
        String userId = "paul";
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        ThrowMoneyRequestDTO requestDTO = new ThrowMoneyRequestDTO();
        requestDTO.setTotalMoney(10000);
        requestDTO.setThrowPeople(5);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<ThrowMoneyRequestDTO> entity = new HttpEntity<>(requestDTO, headers);

        String url = "http://localhost:" + port + "/money/throw";

        ResponseEntity<ThrowMoneyResponseDTO> responseEntity = restTemplate.postForEntity(url, entity, ThrowMoneyResponseDTO.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getThrowUserId()).isEqualTo(userId);
        assertThat(responseEntity.getBody().getMaxMoney()).isLessThan(requestDTO.getTotalMoney());

        List<MoneyDivision> moneyDivisionList = moneyDivisionRepository.findAll();
        assertThat(moneyDivisionList.size()).isEqualTo(requestDTO.getThrowPeople());

        MoneyDivision moneyDivision = moneyDivisionList.get(0);
        System.out.println(moneyDivision.getThrowMoneyDetail());

    }

}
