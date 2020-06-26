package com.kakaopay.homework.exception;

public enum ErrorCode {

    E0001("E0001", "유효시간이 지났습니다."),
    E0002("E0002", "대화방에 포함되어 있지 않습니다."),
    E0003("E0003", "뿌리기한 사람은 가져갈 수 없습니다."),
    E0004("E0004", "한번만 가져갈 수 있습니다."),
    E0005("E0005", "본인만 조회할 수 있습니다."),
    E0006("E0006", "유효하지 않은 Token 입니다."),
    E0007("E0007", "뿌리기 완료");

    private final String code;
    private final String desc;

    ErrorCode(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
