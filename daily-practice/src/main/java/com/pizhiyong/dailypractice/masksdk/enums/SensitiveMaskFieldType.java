package com.pizhiyong.dailypractice.masksdk.enums;

public enum SensitiveMaskFieldType {
    PHONE("phone", "手机号"),
    ID_CARD("idCard", "身份证"),
    BANK_CARD("bankCard", "银行卡"),
    EMAIL("email", "邮箱"),
    ADDRESS("address", "地址"),
    NAME("name", "姓名"),
    ;

    private final String type;
    private final String desc;

    SensitiveMaskFieldType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }
}
