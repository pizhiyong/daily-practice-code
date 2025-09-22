package com.pizhiyong.dailypractice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 配置工具类
 * 模拟原有的WeConfig功能
 *
 * @author pizhiyong
 * @since 1.0
 */
@Component
public class WeConfig {
    
    @Autowired
    private Environment environment;
    
    // 配置缓存
    private static final Map<String, Object> configCache = new ConcurrentHashMap<>();
    
    /**
     * 获取布尔配置
     *
     * @param key 配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    public static boolean getBooleanConfig(String key, boolean defaultValue) {
        return (Boolean) configCache.computeIfAbsent(key, k -> {
            // 这里可以从各种配置源读取，如数据库、配置中心等
            // 目前简化为从Spring Environment读取
            return getEnvironment().getProperty(k, Boolean.class, defaultValue);
        });
    }
    
    /**
     * 获取字符串配置
     */
    public static String getStringConfig(String key, String defaultValue) {
        return (String) configCache.computeIfAbsent(key, k -> 
            getEnvironment().getProperty(k, String.class, defaultValue));
    }
    
    /**
     * 获取整数配置
     */
    public static int getIntConfig(String key, int defaultValue) {
        return (Integer) configCache.computeIfAbsent(key, k -> 
            getEnvironment().getProperty(k, Integer.class, defaultValue));
    }
    
    /**
     * 清除配置缓存
     */
    public static void clearCache() {
        configCache.clear();
    }
    
    /**
     * 清除指定配置缓存
     */
    public static void clearCache(String key) {
        configCache.remove(key);
    }
    
    // 静态Environment实例（简化实现）
    private static Environment staticEnvironment;
    
    @Autowired
    public void setEnvironment(Environment environment) {
        WeConfig.staticEnvironment = environment;
    }
    
    private static Environment getEnvironment() {
        return staticEnvironment;
    }
}
