package com.pizhiyong.dailypractice.util;

/**
 * 随机数生成工具
 *
 */

import java.util.concurrent.ThreadLocalRandom;

public class RandomUtils {

    static char[] CHARS = "0123456789".toCharArray();

    /**
     * 带前缀的纯随机数字生成
     * @param prefix
     * @return
     */
    public static String genDspId(String prefix) {
        StringBuilder sb = new StringBuilder(6);
        sb.append(prefix);
        ThreadLocalRandom tlr = ThreadLocalRandom.current();
        for (int i = 0; i < 5; i++) {
            sb.append(CHARS[tlr.nextInt(0, CHARS.length)]);
        }
        return sb.toString();
    }
}
