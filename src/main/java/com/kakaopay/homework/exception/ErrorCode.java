package com.kakaopay.homework.exception;

public enum ErrorCode {

    E0001("E0001", "유효시간이 지났습니다."),
    E0002("E0002", "대화방에 포함되어 있지 않습니다."),
    E0003("E0003", "뿌리기한 사람은 가져갈수 없습니다."),
    E0004("E0004", "이미 가져가셨습니다.")
    ;

    private String code;
    private String desc;

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
