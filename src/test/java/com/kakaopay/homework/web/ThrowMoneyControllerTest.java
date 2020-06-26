package com.kakaopay.homework.web;

import com.kakaopay.homework.domain.money.MoneyDivision;
import com.kakaopay.homework.domain.money.MoneyDivisionRepository;
import com.kakaopay.homework.domain.money.ThrowMoneyDetail;
import com.kakaopay.homework.domain.money.ThrowMoneyDetailRepository;
import com.kakaopay.homework.domain.user.ThrowUser;
import com.kakaopay.homework.domain.user.ThrowUserRepository;
import com.kakaopay.homework.service.ThrowMoneyService;
import com.kakaopay.homework.web.dto.GetMoneyResponseDTO;
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

    @Autowired
    private ThrowUserRepository throwUserRepository;

    @After
    public void tearDown() {
        throwMoneyDetailRepository.deleteAll();
        moneyDivisionRepository.deleteAll();
    }

    @Test
    public void throwMoney() {
        String userId = "paul";
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        ThrowMoneyRequestDTO requestDTO = new ThrowMoneyRequestDTO();
        requestDTO.setTotalMoney(1000);
        requestDTO.setThrowPeople(3);

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

//        for (MoneyDivision moneyDivision1 : moneyDivisionList) {
//            System.out.println(moneyDivision1.getDividedMoney());
//        }

        List<ThrowMoneyDetail> throwMoneyDetailList = throwMoneyDetailRepository.findAll();
        assertThat(moneyDivision.getToken()).isEqualTo(throwMoneyDetailList.get(0).getToken());

    }

    @Test
    public void getMoney() {
        String userId = "paul";
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        ThrowUser throwUser = new ThrowUser();
        throwUser.setUserId(userId);
        throwUser.setChatRoomId(chatRoomId);
        throwUserRepository.save(throwUser);

        ThrowMoneyRequestDTO requestDTO = new ThrowMoneyRequestDTO();
        requestDTO.setTotalMoney(1000);
        requestDTO.setThrowPeople(3);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<ThrowMoneyRequestDTO> entity = new HttpEntity<>(requestDTO, headers);
        String url = "http://localhost:" + port + "/money/throw";
        ResponseEntity<ThrowMoneyResponseDTO> responseEntity = restTemplate.postForEntity(url, entity, ThrowMoneyResponseDTO.class);

        String token = responseEntity.getBody().getToken();

        userId = "josie";
        throwUser.setUserId(userId);
        throwUser.setChatRoomId(chatRoomId);
        throwUserRepository.save(throwUser);

        headers.set("X-USER-ID", userId);
        HttpEntity<?> getMoneyEntity = new HttpEntity<>(headers);

        url = "http://localhost:" + port + "/money/get/" + token;

        ResponseEntity<GetMoneyResponseDTO> getResponseEntity = restTemplate.postForEntity(url, getMoneyEntity, GetMoneyResponseDTO.class);

//        MoneyDivision moneyDivision = moneyDivisionRepository.findByUserIdAndToken(userId, token);

//        assertThat(getResponseEntity.getBody().getMoney()).isEqualTo(moneyDivision.getDividedMoney());
    }

}
