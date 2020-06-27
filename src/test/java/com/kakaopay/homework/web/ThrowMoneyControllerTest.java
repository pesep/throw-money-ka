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

    // 돈 뿌리기 테스트
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

        List<ThrowMoneyDetail> throwMoneyDetailList = throwMoneyDetailRepository.findAll();
        assertThat(moneyDivision.getToken()).isEqualTo(throwMoneyDetailList.get(0).getToken());

    }

    // 뿌린 돈 받기 테스트
    @Test
    public void getMoney() {

        // 테스트 데이터
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        List<ThrowUser> throwUserList = new ArrayList<>();
        ThrowUser throwUser1 = new ThrowUser();
        throwUser1.setUserId("marduk");
        throwUser1.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser1);

        ThrowUser throwUser2 = new ThrowUser();
        throwUser2.setUserId("josie");
        throwUser2.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser2);

        ThrowUser throwUser3 = new ThrowUser();
        throwUser3.setUserId("katarina");
        throwUser3.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser3);

        for (ThrowUser throwUser : throwUserList) {
            throwUserRepository.save(throwUser);
        }

        ThrowMoneyDetail throwMoneyDetailData = new ThrowMoneyDetail();
        throwMoneyDetailData.setToken(RandomStringUtils.randomAlphanumeric(3));
        throwMoneyDetailData.setThrowPeople(5);
        throwMoneyDetailData.setTotalMoney(1000);
        throwMoneyDetailData.setMoneyMaker(throwUser1.getUserId());
        throwMoneyDetailData.setGroupChatId(chatRoomId);
        throwMoneyDetailData.setCreatedDate(LocalDateTime.now());
        throwMoneyDetailData.setModifiedDate(LocalDateTime.now());
        throwMoneyDetailRepository.save(throwMoneyDetailData);

        List<MoneyDivision> moneyDivisionList = new ArrayList<>();
        MoneyDivision moneyDivision1 = new MoneyDivision();
        moneyDivision1.setToken(throwMoneyDetailData.getToken());
        moneyDivision1.setDividedMoney(553);
        moneyDivisionList.add(moneyDivision1);

        MoneyDivision moneyDivision2 = new MoneyDivision();
        moneyDivision2.setToken(throwMoneyDetailData.getToken());
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

        String token = throwMoneyDetailData.getToken();

        String userId = "josie";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> getMoneyEntity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        ResponseEntity<GetMoneyResponseDTO> getResponseEntity = restTemplate.postForEntity(url, getMoneyEntity, GetMoneyResponseDTO.class);

        MoneyDivision moneyDivision = moneyDivisionRepository.findByReceivedMoneyUserIdAndToken(userId, token);

        assertThat(getResponseEntity.getBody().getMoney()).isEqualTo(moneyDivision.getDividedMoney());
    }

    // 뿌리기 조회 테스트
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

        String token = throwMoneyDetailData.getToken();
        String userId = throwMoneyDetailData.getMoneyMaker();
        String chatRoomId = throwMoneyDetailData.getGroupChatId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        ResponseEntity<GetThrowMoneyDetailResponseDTO> responseEntity = restTemplate.exchange(url, HttpMethod.GET, entity, GetThrowMoneyDetailResponseDTO.class);

        assertThat(responseEntity.getBody().getThrowMoney()).isEqualTo(throwMoneyDetailData.getTotalMoney());
        assertThat(responseEntity.getBody().getThrowDateTime()).isEqualTo(throwMoneyDetailData.getCreatedDate());

    }

    // 뿌리고 10분 후 체크
    @Test
    public void limit10MinuteCheck() {

        // 테스트 데이터
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        List<ThrowUser> throwUserList = new ArrayList<>();
        ThrowUser throwUser1 = new ThrowUser();
        throwUser1.setUserId("marduk");
        throwUser1.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser1);

        ThrowUser throwUser2 = new ThrowUser();
        throwUser2.setUserId("josie");
        throwUser2.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser2);

        ThrowUser throwUser3 = new ThrowUser();
        throwUser3.setUserId("katarina");
        throwUser3.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser3);

        for (ThrowUser throwUser : throwUserList) {
            throwUserRepository.save(throwUser);
        }

        ThrowMoneyDetail throwMoneyDetailData = new ThrowMoneyDetail();
        throwMoneyDetailData.setToken(RandomStringUtils.randomAlphanumeric(3));
        throwMoneyDetailData.setThrowPeople(5);
        throwMoneyDetailData.setTotalMoney(1000);
        throwMoneyDetailData.setMoneyMaker(throwUser1.getUserId());
        throwMoneyDetailData.setGroupChatId(chatRoomId);
        throwMoneyDetailData.setCreatedDate(LocalDateTime.now().minusMinutes(11)); // 11분 전 데이터
        throwMoneyDetailData.setModifiedDate(LocalDateTime.now().minusMinutes(11));
        throwMoneyDetailRepository.save(throwMoneyDetailData);

        List<MoneyDivision> moneyDivisionList = new ArrayList<>();
        MoneyDivision moneyDivision = new MoneyDivision();
        moneyDivision.setToken(throwMoneyDetailData.getToken());
        moneyDivision.setDividedMoney(553);
        moneyDivision.setReceivedMoneyUserId(throwUser2.getUserId());
        moneyDivisionList.add(moneyDivision);

        MoneyDivision moneyDivision2 = new MoneyDivision();
        moneyDivision2.setToken(throwMoneyDetailData.getToken());
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
        String userId = throwUser3.getUserId();
        chatRoomId = throwMoneyDetail.get(0).getGroupChatId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> getMoneyEntity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        restTemplate.postForEntity(url, getMoneyEntity, GetMoneyResponseDTO.class);

    }

    // 대화방에 없는 사람 체크
    @Test
    public void notInChatRoomCheck() {

        // 테스트 데이터
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        List<ThrowUser> throwUserList = new ArrayList<>();
        ThrowUser throwUser1 = new ThrowUser();
        throwUser1.setUserId("marduk");
        throwUser1.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser1);

        ThrowUser throwUser2 = new ThrowUser();
        throwUser2.setUserId("josie");
        throwUser2.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser2);

        ThrowUser throwUser3 = new ThrowUser();
        throwUser3.setUserId("katarina");
        throwUser3.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser3);

        for (ThrowUser throwUser : throwUserList) {
            throwUserRepository.save(throwUser);
        }

        ThrowMoneyDetail throwMoneyDetailData = new ThrowMoneyDetail();
        throwMoneyDetailData.setToken(RandomStringUtils.randomAlphanumeric(3));
        throwMoneyDetailData.setThrowPeople(5);
        throwMoneyDetailData.setTotalMoney(1000);
        throwMoneyDetailData.setMoneyMaker(throwUser1.getUserId());
        throwMoneyDetailData.setGroupChatId(chatRoomId);
        throwMoneyDetailData.setCreatedDate(LocalDateTime.now());
        throwMoneyDetailData.setModifiedDate(LocalDateTime.now());
        throwMoneyDetailRepository.save(throwMoneyDetailData);

        List<MoneyDivision> moneyDivisionList = new ArrayList<>();
        MoneyDivision moneyDivision = new MoneyDivision();
        moneyDivision.setToken(throwMoneyDetailData.getToken());
        moneyDivision.setDividedMoney(553);
        moneyDivision.setReceivedMoneyUserId(throwUser2.getUserId());
        moneyDivisionList.add(moneyDivision);

        MoneyDivision moneyDivision2 = new MoneyDivision();
        moneyDivision2.setToken(throwMoneyDetailData.getToken());
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
        chatRoomId = throwMoneyDetail.get(0).getGroupChatId();

        String userId = "steve"; // 채팅방에 없는 사람

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> getMoneyEntity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        restTemplate.postForEntity(url, getMoneyEntity, GetMoneyResponseDTO.class);

    }

    // 돈 뿌린 사람 체크
    @Test
    public void noGetMoneyMakerCheck() {

        // 테스트 데이터
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        List<ThrowUser> throwUserList = new ArrayList<>();
        ThrowUser throwUser1 = new ThrowUser();
        throwUser1.setUserId("marduk");
        throwUser1.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser1);

        ThrowUser throwUser2 = new ThrowUser();
        throwUser2.setUserId("josie");
        throwUser2.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser2);

        ThrowUser throwUser3 = new ThrowUser();
        throwUser3.setUserId("katarina");
        throwUser3.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser3);

        for (ThrowUser throwUser : throwUserList) {
            throwUserRepository.save(throwUser);
        }

        ThrowMoneyDetail throwMoneyDetailData = new ThrowMoneyDetail();
        throwMoneyDetailData.setToken(RandomStringUtils.randomAlphanumeric(3));
        throwMoneyDetailData.setThrowPeople(5);
        throwMoneyDetailData.setTotalMoney(1000);
        throwMoneyDetailData.setMoneyMaker(throwUser1.getUserId());
        throwMoneyDetailData.setGroupChatId(chatRoomId);
        throwMoneyDetailData.setCreatedDate(LocalDateTime.now());
        throwMoneyDetailData.setModifiedDate(LocalDateTime.now());
        throwMoneyDetailRepository.save(throwMoneyDetailData);

        List<MoneyDivision> moneyDivisionList = new ArrayList<>();
        MoneyDivision moneyDivision = new MoneyDivision();
        moneyDivision.setToken(throwMoneyDetailData.getToken());
        moneyDivision.setDividedMoney(553);
        moneyDivision.setReceivedMoneyUserId(throwUser2.getUserId());
        moneyDivisionList.add(moneyDivision);

        MoneyDivision moneyDivision2 = new MoneyDivision();
        moneyDivision2.setToken(throwMoneyDetailData.getToken());
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
        chatRoomId = throwMoneyDetail.get(0).getGroupChatId();

        String userId = throwUser1.getUserId(); // 돈 뿌린 사람은 못가져감

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> getMoneyEntity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        restTemplate.postForEntity(url, getMoneyEntity, GetMoneyResponseDTO.class);

    }

    // 가져간 사람은 못 가져감
    @Test
    public void canNotTakeBackCheck() {

        // 테스트 데이터
        String chatRoomId = RandomStringUtils.randomAlphanumeric(10);

        List<ThrowUser> throwUserList = new ArrayList<>();
        ThrowUser throwUser1 = new ThrowUser();
        throwUser1.setUserId("marduk");
        throwUser1.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser1);

        ThrowUser throwUser2 = new ThrowUser();
        throwUser2.setUserId("josie");
        throwUser2.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser2);

        ThrowUser throwUser3 = new ThrowUser();
        throwUser3.setUserId("katarina");
        throwUser3.setChatRoomId(chatRoomId);
        throwUserList.add(throwUser3);

        for (ThrowUser throwUser : throwUserList) {
            throwUserRepository.save(throwUser);
        }

        ThrowMoneyDetail throwMoneyDetailData = new ThrowMoneyDetail();
        throwMoneyDetailData.setToken(RandomStringUtils.randomAlphanumeric(3));
        throwMoneyDetailData.setThrowPeople(5);
        throwMoneyDetailData.setTotalMoney(1000);
        throwMoneyDetailData.setMoneyMaker(throwUser1.getUserId());
        throwMoneyDetailData.setGroupChatId(chatRoomId);
        throwMoneyDetailData.setCreatedDate(LocalDateTime.now());
        throwMoneyDetailData.setModifiedDate(LocalDateTime.now());
        throwMoneyDetailRepository.save(throwMoneyDetailData);

        List<MoneyDivision> moneyDivisionList = new ArrayList<>();
        MoneyDivision moneyDivision = new MoneyDivision();
        moneyDivision.setToken(throwMoneyDetailData.getToken());
        moneyDivision.setDividedMoney(553);
        moneyDivision.setReceivedMoneyUserId(throwUser2.getUserId());
        moneyDivisionList.add(moneyDivision);

        MoneyDivision moneyDivision2 = new MoneyDivision();
        moneyDivision2.setToken(throwMoneyDetailData.getToken());
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
        chatRoomId = throwMoneyDetail.get(0).getGroupChatId();

        String userId = throwUser2.getUserId(); // 가져간 사람은 못가져감

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> getMoneyEntity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        restTemplate.postForEntity(url, getMoneyEntity, GetMoneyResponseDTO.class);

    }

    // 조회 - 7일간 조회 가능
    @Test
    public void limit7DaysCheck() {
        // 테스트 데이터
        ThrowMoneyDetail throwMoneyDetailData = new ThrowMoneyDetail();
        throwMoneyDetailData.setToken(RandomStringUtils.randomAlphanumeric(3));
        throwMoneyDetailData.setThrowPeople(5);
        throwMoneyDetailData.setTotalMoney(1000);
        throwMoneyDetailData.setMoneyMaker("marduk");
        throwMoneyDetailData.setGroupChatId(RandomStringUtils.randomAlphanumeric(10));
        throwMoneyDetailData.setCreatedDate(LocalDateTime.now().minusDays(8)); // 생성 날짜를 8일 전 으로 설정
        throwMoneyDetailData.setModifiedDate(LocalDateTime.now().minusDays(8));
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

        String token = throwMoneyDetailData.getToken();
        String userId = throwMoneyDetailData.getMoneyMaker();
        String chatRoomId = throwMoneyDetailData.getGroupChatId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        restTemplate.exchange(url, HttpMethod.GET, entity, GetThrowMoneyDetailResponseDTO.class);
    }

    // 조회 - token 체크
    @Test
    public void tokenCheck() {
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

        String token = "AbC";
        String userId = throwMoneyDetailData.getMoneyMaker();
        String chatRoomId = throwMoneyDetailData.getGroupChatId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        restTemplate.exchange(url, HttpMethod.GET, entity, GetThrowMoneyDetailResponseDTO.class);
    }

    // 조회 - 뿌린사람 본인만 조회 가능
    @Test
    public void moneyMakerCheck() {
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

        String token = throwMoneyDetailData.getToken();
        String userId = "josie";
        String chatRoomId = throwMoneyDetailData.getGroupChatId();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-USER-ID", userId);
        headers.set("X-ROOM-ID", chatRoomId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "http://localhost:" + port + "/money/get/" + token;

        restTemplate.exchange(url, HttpMethod.GET, entity, GetThrowMoneyDetailResponseDTO.class);
    }

}
