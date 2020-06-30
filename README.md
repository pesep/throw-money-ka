# ka-throw
## 개발환경
* 언어 : Java 8
* 프레임워크 : SpringBoot 2.1.7
* 데이터베이스 : H2

## 문제 해결 전략

### Entity 정의
#### ThrowMoneyDetail
돈 뿌리기의 일반적인 정보들을 나타냅니다.
- `ThrowMoneyDetail`과 `MoneyDivision`은 1:N 관계로 이루어져있습니다.

#### MoneyDivision
뿌려진 돈의 배분과 대화방 정보, 가져간 사람의 정보를 나타냅니다.
- PK는 구분할값이 따로 없다고 판단해서 sequence로 줬습니다.

#### ThrowUser
대화방과 대화방에 소속된 사람 정보를 나타냅니다.
- Primary Key는 `@IdClass` 방법으로 설정했습니다.

### 주요 로직
#### 돈 뿌리기
- 금액 분배를 인원에 맞게 미리 분배해서 테이블에 저장했습니다.

#### 돈 가져가기
- 제약사항들을 먼저 체크하고 미리 분배된 금액을 차례대로 가져갑니다.

#### 뿌리기 정보 조회
- 제약사항들을 먼저 체크하고 데이터를 조회합니다.
- 받은 사람 리스트는 돈을 받아간 사람만 DTO에 따로 조회합니다.

## API 목록
3개 API로 구성됐습니다.
### 돈 뿌리기
#### Request
```
curl --location --request POST 'localhost:8080/money/throw' \
--header 'X-USER-ID: king' \
--header 'X-ROOM-ID: 9shKanauPo' \
--header 'Content-Type: application/json' \
--data-raw '{
    "totalMoney" : 10000,
    "throwPeople" : 5
}'
```
#### Response
```
{
    "token": "UOB",
    "maxMoney": 4804,
    "throwUserId": "king"
}
```
### 돈 가져가기
#### Request
```
curl --location --request POST 'localhost:8080/money/get/UOB' \
--header 'X-USER-ID: josie' \
--header 'X-ROOM-ID: 9shKanauPo' \
```
#### Response
```
{
    "money": 1726
}
```
### 뿌리기 정보 조회
#### Request
```
curl --location --request GET 'localhost:8080/money/get/UOB' \
--header 'X-USER-ID: king' \
--header 'X-ROOM-ID: 9shKanauPo'
```
#### Response
```
{
    "throwDateTime": "2020-06-27T14:37:23.261",
    "throwMoney": 10000,
    "receivedMoney": 7247,
    "moneyDivisionDTOList": [
        {
            "receivedMoney": 1726,
            "receivedUserId": "josie"
        },
        {
            "receivedMoney": 5521,
            "receivedUserId": "katarina"
        }
    ]
}
```
