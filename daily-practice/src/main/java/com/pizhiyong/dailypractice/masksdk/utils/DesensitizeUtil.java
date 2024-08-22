package com.pizhiyong.dailypractice.masksdk.utils;

import com.pizhiyong.dailypractice.masksdk.enums.SensitiveMaskFieldType;
import org.apache.commons.lang3.StringUtils;

/**
 * 脱敏工具类
 **/
public class DesensitizeUtil {

    /**
     * 用户名
     */
    public static String userName(String userName) {
        return left(userName, 0);
    }


    /**
     * 身份证
     */
    public static String idCard(String cardNo) {
        return left(cardNo, 4);
    }

    /**
     * 银行卡
     */
    public static String bankCard(String cardNo) {
        return left(cardNo, 6);
    }

    /**
     * 地址
     */
    public static String address(String address) {
        return address(address, 12);
    }

    /**
     * 电话
     */
    public static String phone(String phoneNo) {
        return right(phoneNo, 4);
    }

    /**
     * 只显示第一个汉字，其他隐藏为2个星号<例子：李**>
     *
     * @param fullName
     * @param index    1 为第index位
     * @return
     */
    public static String left(String fullName, int index) {
        if (StringUtils.isBlank(fullName)) {
            return "";
        }
        String name = StringUtils.left(fullName, index);
        return StringUtils.rightPad(name, StringUtils.length(fullName), "*");
    }

    /**
     * [身份证号] 110****58，前面保留3位明文，后面保留2位明文
     *
     * @param name
     * @param index 3
     * @param end   2
     * @return
     */
    public static String around(String name, int index, int end) {
        if (StringUtils.isBlank(name)) {
            return "";
        }
        return StringUtils.left(name, index).concat(StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(name, end), StringUtils.length(name), "*"), "***"));
    }

    /**
     * [固定电话] 后四位，其他隐藏<例子：****1234>
     *
     * @param num
     * @return
     */
    public static String right(String num, int end) {
        if (StringUtils.isBlank(num)) {
            return "";
        }
        return StringUtils.leftPad(StringUtils.right(num, end), StringUtils.length(num), "*");
    }

    /**
     * [地址] 只显示到地区，不显示详细地址；我们要对个人信息增强保护<例子：北京市海淀区****>
     *
     * @param address
     * @param sensitiveSize 敏感信息长度
     * @return
     */
    public static String address(String address, int sensitiveSize) {
        if (StringUtils.isBlank(address)) {
            return "";
        }
        int length = StringUtils.length(address);
        return StringUtils.rightPad(StringUtils.left(address, length - sensitiveSize), length, "*");
    }

    /**
     * [电子邮箱] 邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示<例子:g**@163.com>
     *
     * @param email
     * @return
     */
    public static String email(String email) {
        if (StringUtils.isBlank(email)) {
            return "";
        }
        int index = StringUtils.indexOf(email, "@");
        if (index <= 1) {
            return email;
        } else {
            return StringUtils.rightPad(StringUtils.left(email, 1), index, "*").concat(StringUtils.mid(email, index, StringUtils.length(email)));

        }
    }


    /**
     * 根据脱敏类型调用相应的脱敏方法
     *
     * @param str
     * @param encryptEnum
     * @return
     */
    public static String desensitize(String str, SensitiveMaskFieldType encryptEnum) {
        return switch (encryptEnum) {
            case ID_CARD -> idCard(str);
            case BANK_CARD -> bankCard(str);
            case ADDRESS -> address(str);
            case PHONE -> phone(str);
            case EMAIL -> email(str);
            case NAME -> userName(str);
        };

    }

}
