package com.pizhiyong.dailypractice.masksdk.enums;

public enum SensitiveMaskHandleType {
    MASK_ALL("mask_all", "全部脱敏"),
    MASK_PART("mask_part", "部分脱敏"),
    MASK_NONE("mask_none", "不脱敏");

    private final String code;
    private final String desc;

    SensitiveMaskHandleType(String code, String desc) {
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
