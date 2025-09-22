package com.pizhiyong.dailypractice.util;

/**
 * RandomUtils工具类功能演示
 */
public class RandomUtilsDemo {
    
    public static void main(String[] args) {
        System.out.println("=== RandomUtils 工具类功能演示 ===\n");
        
        // 1. 原有功能（向后兼容）
        System.out.println("1. 原有功能（向后兼容）:");
        System.out.println("genDspId(\"DSP\"): " + RandomUtils.genDspId("DSP"));
        System.out.println("genDspId(\"ORDER\"): " + RandomUtils.genDspId("ORDER"));
        System.out.println();
        
        // 2. 带前缀的随机字符串生成
        System.out.println("2. 带前缀的随机字符串生成:");
        System.out.println("数字前缀: " + RandomUtils.generateWithPrefix("NUM", 8, RandomUtils.CharacterSet.DIGITS));
        System.out.println("字母前缀: " + RandomUtils.generateWithPrefix("STR", 6, RandomUtils.CharacterSet.LETTERS));
        System.out.println("混合前缀: " + RandomUtils.generateWithPrefix("MIX", 10, RandomUtils.CharacterSet.ALPHANUMERIC));
        System.out.println();
        
        // 3. 各种类型的随机字符串
        System.out.println("3. 各种类型的随机字符串:");
        System.out.println("纯数字(8位): " + RandomUtils.generateDigits(8));
        System.out.println("纯字母(10位): " + RandomUtils.generateLetters(10));
        System.out.println("字母数字(12位): " + RandomUtils.generateAlphanumeric(12));
        System.out.println("小写字母(6位): " + RandomUtils.generateString(6, RandomUtils.CharacterSet.LETTERS_LOWER));
        System.out.println("大写字母(6位): " + RandomUtils.generateString(6, RandomUtils.CharacterSet.LETTERS_UPPER));
        System.out.println();
        
        // 4. 随机数字范围生成
        System.out.println("4. 随机数字范围生成:");
        System.out.println("随机整数(1-100): " + RandomUtils.generateInt(1, 100));
        System.out.println("随机整数(50-60): " + RandomUtils.generateInt(50, 60));
        System.out.println("随机长整数(1000-9999): " + RandomUtils.generateLong(1000L, 9999L));
        System.out.println();
        
        // 5. UUID生成
        System.out.println("5. UUID生成:");
        System.out.println("标准UUID: " + RandomUtils.generateStandardUUID());
        System.out.println("无连字符UUID: " + RandomUtils.generateUUID());
        System.out.println();
        
        // 6. 批量生成演示
        System.out.println("6. 批量生成演示（生成5个随机ID）:");
        for (int i = 1; i <= 5; i++) {
            System.out.println("ID" + i + ": " + RandomUtils.generateWithPrefix("USER", 8, RandomUtils.CharacterSet.ALPHANUMERIC));
        }
        System.out.println();
        
        // 7. 性能测试
        System.out.println("7. 性能测试（生成10000个随机字符串）:");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            RandomUtils.generateAlphanumeric(16);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("耗时: " + (endTime - startTime) + "ms");
        System.out.println();
        
        System.out.println("=== 演示完成 ===");
    }
}
