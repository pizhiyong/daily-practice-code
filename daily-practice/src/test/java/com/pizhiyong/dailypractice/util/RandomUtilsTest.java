package com.pizhiyong.dailypractice.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.RepeatedTest;
import static org.junit.jupiter.api.Assertions.*;

/**
 * RandomUtils工具类测试
 */
public class RandomUtilsTest {

    @Test
    public void testGenDspId() {
        String prefix = "DSP";
        String result = RandomUtils.genDspId(prefix);
        
        assertNotNull(result);
        assertTrue(result.startsWith(prefix));
        assertEquals(prefix.length() + 5, result.length());
        
        // 验证随机部分都是数字
        String randomPart = result.substring(prefix.length());
        assertTrue(randomPart.matches("\\d{5}"));
    }

    @Test
    public void testGenDspIdWithNullPrefix() {
        assertThrows(IllegalArgumentException.class, () -> {
            RandomUtils.genDspId(null);
        });
    }

    @Test
    public void testGenerateWithPrefix() {
        String prefix = "TEST";
        int length = 8;
        String result = RandomUtils.generateWithPrefix(prefix, length, RandomUtils.CharacterSet.ALPHANUMERIC);
        
        assertNotNull(result);
        assertTrue(result.startsWith(prefix));
        assertEquals(prefix.length() + length, result.length());
    }

    @Test
    public void testGenerateString() {
        int length = 10;
        String result = RandomUtils.generateString(length, RandomUtils.CharacterSet.LETTERS);
        
        assertNotNull(result);
        assertEquals(length, result.length());
        assertTrue(result.matches("[a-zA-Z]{10}"));
    }

    @Test
    public void testGenerateDigits() {
        int length = 6;
        String result = RandomUtils.generateDigits(length);
        
        assertNotNull(result);
        assertEquals(length, result.length());
        assertTrue(result.matches("\\d{6}"));
    }

    @Test
    public void testGenerateLetters() {
        int length = 8;
        String result = RandomUtils.generateLetters(length);
        
        assertNotNull(result);
        assertEquals(length, result.length());
        assertTrue(result.matches("[a-zA-Z]{8}"));
    }

    @Test
    public void testGenerateAlphanumeric() {
        int length = 12;
        String result = RandomUtils.generateAlphanumeric(length);
        
        assertNotNull(result);
        assertEquals(length, result.length());
        assertTrue(result.matches("[a-zA-Z0-9]{12}"));
    }

    @Test
    public void testGenerateInt() {
        int min = 10;
        int max = 20;
        int result = RandomUtils.generateInt(min, max);
        
        assertTrue(result >= min && result <= max);
    }

    @Test
    public void testGenerateIntInvalidRange() {
        assertThrows(IllegalArgumentException.class, () -> {
            RandomUtils.generateInt(20, 10);
        });
    }

    @Test
    public void testGenerateLong() {
        long min = 100L;
        long max = 200L;
        long result = RandomUtils.generateLong(min, max);
        
        assertTrue(result >= min && result <= max);
    }

    @Test
    public void testGenerateUUID() {
        String result = RandomUtils.generateUUID();
        
        assertNotNull(result);
        assertEquals(32, result.length());
        assertFalse(result.contains("-"));
        assertTrue(result.matches("[a-f0-9]{32}"));
    }

    @Test
    public void testGenerateStandardUUID() {
        String result = RandomUtils.generateStandardUUID();
        
        assertNotNull(result);
        assertEquals(36, result.length());
        assertTrue(result.contains("-"));
        assertTrue(result.matches("[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}"));
    }

    @Test
    public void testInvalidLength() {
        assertThrows(IllegalArgumentException.class, () -> {
            RandomUtils.generateString(0, RandomUtils.CharacterSet.DIGITS);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            RandomUtils.generateString(-1, RandomUtils.CharacterSet.DIGITS);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            RandomUtils.generateString(10001, RandomUtils.CharacterSet.DIGITS);
        });
    }

    @Test
    public void testNullCharacterSet() {
        assertThrows(IllegalArgumentException.class, () -> {
            RandomUtils.generateString(5, null);
        });
    }

    @RepeatedTest(10)
    public void testRandomnessConsistency() {
        // 测试多次生成的结果应该不同（虽然理论上可能相同，但概率极低）
        String result1 = RandomUtils.generateAlphanumeric(20);
        String result2 = RandomUtils.generateAlphanumeric(20);
        
        assertNotEquals(result1, result2, "Generated strings should be different");
    }

    @Test
    public void testAllCharacterSets() {
        // 测试所有字符集类型
        for (RandomUtils.CharacterSet charSet : RandomUtils.CharacterSet.values()) {
            String result = RandomUtils.generateString(10, charSet);
            assertNotNull(result);
            assertEquals(10, result.length());
        }
    }

    @Test
    public void testUtilityClassCannotBeInstantiated() {
        assertThrows(UnsupportedOperationException.class, () -> {
            // 使用反射尝试创建实例
            java.lang.reflect.Constructor<RandomUtils> constructor = 
                RandomUtils.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
