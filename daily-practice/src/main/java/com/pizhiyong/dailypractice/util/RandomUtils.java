package com.pizhiyong.dailypractice.util;

import java.util.concurrent.ThreadLocalRandom;
import java.util.UUID;

/**
 * 随机数生成工具类
 * 提供各种类型的随机字符串、数字生成功能
 * 线程安全，基于ThreadLocalRandom实现
 *
 * @author pizhiyong
 * @since 1.0
 */
public class RandomUtils {

    // 字符集常量
    private static final char[] DIGITS = "0123456789".toCharArray();
    private static final char[] LETTERS_LOWER = "abcdefghijklmnopqrstuvwxyz".toCharArray();
    private static final char[] LETTERS_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    private static final char[] ALPHANUMERIC = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * 私有构造函数，防止实例化
     */
    private RandomUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * 生成带前缀的随机数字字符串（保持向后兼容）
     *
     * @param prefix 前缀字符串
     * @return 带前缀的随机字符串，格式为：prefix + 5位随机数字
     * @throws IllegalArgumentException 如果prefix为null
     */
    public static String genDspId(String prefix) {
        return generateWithPrefix(prefix, 5, CharacterSet.DIGITS);
    }

    /**
     * 生成带前缀的随机字符串
     *
     * @param prefix 前缀字符串
     * @param length 随机部分的长度
     * @param characterSet 字符集类型
     * @return 带前缀的随机字符串
     * @throws IllegalArgumentException 如果参数无效
     */
    public static String generateWithPrefix(String prefix, int length, CharacterSet characterSet) {
        validatePrefix(prefix);
        validateLength(length);
        validateCharacterSet(characterSet);

        char[] chars = getCharArray(characterSet);
        StringBuilder sb = new StringBuilder(prefix.length() + length);
        sb.append(prefix);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }

        return sb.toString();
    }

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @param characterSet 字符集类型
     * @return 随机字符串
     * @throws IllegalArgumentException 如果参数无效
     */
    public static String generateString(int length, CharacterSet characterSet) {
        validateLength(length);
        validateCharacterSet(characterSet);

        char[] chars = getCharArray(characterSet);
        StringBuilder sb = new StringBuilder(length);

        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            sb.append(chars[random.nextInt(chars.length)]);
        }

        return sb.toString();
    }

    /**
     * 生成随机数字字符串
     *
     * @param length 字符串长度
     * @return 随机数字字符串
     * @throws IllegalArgumentException 如果length无效
     */
    public static String generateDigits(int length) {
        return generateString(length, CharacterSet.DIGITS);
    }

    /**
     * 生成随机字母字符串（大小写混合）
     *
     * @param length 字符串长度
     * @return 随机字母字符串
     * @throws IllegalArgumentException 如果length无效
     */
    public static String generateLetters(int length) {
        return generateString(length, CharacterSet.LETTERS);
    }

    /**
     * 生成随机字母数字字符串
     *
     * @param length 字符串长度
     * @return 随机字母数字字符串
     * @throws IllegalArgumentException 如果length无效
     */
    public static String generateAlphanumeric(int length) {
        return generateString(length, CharacterSet.ALPHANUMERIC);
    }

    /**
     * 生成指定范围内的随机整数（包含边界）
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机整数
     * @throws IllegalArgumentException 如果min > max
     */
    public static int generateInt(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    /**
     * 生成指定范围内的随机长整数（包含边界）
     *
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机长整数
     * @throws IllegalArgumentException 如果min > max
     */
    public static long generateLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min cannot be greater than max");
        }
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    /**
     * 生成随机UUID字符串（不包含连字符）
     *
     * @return 32位UUID字符串
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成标准格式的UUID字符串（包含连字符）
     *
     * @return 标准格式UUID字符串
     */
    public static String generateStandardUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 字符集枚举
     */
    public enum CharacterSet {
        /** 数字字符集 0-9 */
        DIGITS,
        /** 小写字母字符集 a-z */
        LETTERS_LOWER,
        /** 大写字母字符集 A-Z */
        LETTERS_UPPER,
        /** 字母字符集 a-z, A-Z */
        LETTERS,
        /** 字母数字字符集 0-9, a-z, A-Z */
        ALPHANUMERIC
    }

    // 私有辅助方法

    private static char[] getCharArray(CharacterSet characterSet) {
        switch (characterSet) {
            case DIGITS:
                return DIGITS;
            case LETTERS_LOWER:
                return LETTERS_LOWER;
            case LETTERS_UPPER:
                return LETTERS_UPPER;
            case LETTERS:
                return LETTERS;
            case ALPHANUMERIC:
                return ALPHANUMERIC;
            default:
                throw new IllegalArgumentException("Unsupported character set: " + characterSet);
        }
    }

    private static void validatePrefix(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("Prefix cannot be null");
        }
    }

    private static void validateLength(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be positive, got: " + length);
        }
        if (length > 10000) {
            throw new IllegalArgumentException("Length too large, maximum allowed: 10000, got: " + length);
        }
    }

    private static void validateCharacterSet(CharacterSet characterSet) {
        if (characterSet == null) {
            throw new IllegalArgumentException("Character set cannot be null");
        }
    }
}
