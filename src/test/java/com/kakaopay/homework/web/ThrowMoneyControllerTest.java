package com.kakaopay.homework.web;

import com.kakaopay.homework.domain.money.MoneyDivision;
import com.kakaopay.homework.domain.money.MoneyDivisionRepository;
import com.kakaopay.homework.domain.money.ThrowMoneyDetail;
import com.kakaopay.homework.domain.money.ThrowMoneyDetailRepository;
import com.kakaopay.homework.domain.user.ThrowUser;
import com.kakaopay.homework.domain.user.ThrowUserRepository;
import com.kakaopay.homework.service.ThrowMoneyService;
import com.kakaopay.homework.web.dto.GetMoneyResponseDTO;
import com.kakaopay.homework.web.dto.GetThrowMoneyDetailResponseDTO;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        for (MoneyDivision moneyDivision1 : moneyDivisionList) {
            System.out.println(moneyDivision1.getDividedMoney());
        }

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

        MoneyDivision moneyDivision = moneyDivisionRepository.findByReceivedMoneyUserIdAndToken(userId, token);

        assertThat(getResponseEntity.getBody().getMoney()).isEqualTo(moneyDivision.getDividedMoney());
    }

    @Test
    public void getThrowMoneyDetail() {

        // 테스트 데이터
        ThrowMoneyDetail throwMoneyDetailData = new ThrowMoneyDetail();
        throwMoneyDetailData.setToken(RandomStringUtils.randomAlphanumeric(3));
        throwMoneyDetailData.setThrowPeople(5);
        throwMoneyDetailData.setTotalMoney(1000);
        throwMoneyDetailData.setMoneyMaker("marduk");
        throwMoneyDetailData.setGroupChatId(RandomStringUtils.randomAlphanumeric(10));
        throwMoneyDetailData.setCreatedDate(LocalDateTime.now());
        throwMoneyDetailData.setModifiedDate(LocalDateTime.now());
        throwMoneyDetailRepository.save(throwMoneyDetailData);

        List<MoneyDivision> moneyDivisionList = new ArrayList<>();
        MoneyDivision moneyDivision = new MoneyDivision();
        moneyDivision.setToken(throwMoneyDetailData.getToken());
        moneyDivision.setDividedMoney(553);
        moneyDivision.setReceivedMoneyUserId("josie");
        moneyDivisionList.add(moneyDivision);

        MoneyDivision moneyDivision2 = new MoneyDivision();
        moneyDivision2.setToken(throwMoneyDetailData.getToken());
        moneyDivision2.setReceivedMoneyUserId("katarina");
        moneyDivision2.setDividedMoney(142);
        moneyDivisionList.add(moneyDivision2);

        MoneyDivision moneyDivision3 = new MoneyDivision();
        moneyDivision3.setToken(throwMoneyDetailData.getToken());
        moneyDivision3.setDividedMoney(60);
        moneyDivisionList.add(moneyDivision3);

        MoneyDivision moneyDivision4 = new MoneyDivision();
        moneyDivision4.setToken(throwMoneyDetailData.getToken());
        moneyDivision4.setDividedMoney(212);
        moneyDivisionList.add(moneyDivision4);

        MoneyDivision moneyDivision5 = new MoneyDivision();
        moneyDivision5.setToken(throwMoneyDetailData.getToken());
        moneyDivision5.setDividedMoney(33);
        moneyDivisionList.add(moneyDivision5);

        for (MoneyDivision data : moneyDivisionList) {
            moneyDivisionRepository.save(data);
        }

        throwMoneyDetailData.setMoneyDivisionList(moneyDivisionList);
        throwMoneyDetailRepository.save(throwMoneyDetailData);

        for (MoneyDivision data : moneyDivisionList) {
            data.setThrowMoneyDetail(throwMoneyDetailData);
            moneyDivisionRepository.save(data);
        }

        List<ThrowMoneyDetail> throwMoneyDetail = throwMoneyDetailRepository.findAll();

        String token = throwMoneyDetail.get(0).getToken();
        String userId = throwMoneyDetail.get(0).getMoneyMaker();
        String chatRoomId = throwMoneyDetail.get(0).getGroupChatId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        ResponseEntity<GetThrowMoneyDetailResponseDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, GetThrowMoneyDetailResponseDTO.class);

        assertThat(responseEntity.getBody().getThrowMoney()).isEqualTo(throwMoneyDetailData.getTotalMoney());
        assertThat(responseEntity.getBody().getThrowDateTime()).isEqualTo(throwMoneyDetailData.getCreatedDate());

    }

}
