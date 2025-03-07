package com.pizhiyong.dailypractice.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.pizhiyong.dailypractice.dao.UserMapper;
import com.pizhiyong.dailypractice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * caffiene 缓存服务
 *
 * @author: tiantipi
 * @create: 2024−11-19 15:04:33
 * @version: 1.0
 */
@Service
public class CaffeineCacheService {

    private static final Logger logger = LoggerFactory.getLogger(CaffeineCacheService.class);

    @Autowired
    private UserMapper userMapper;

    private final Cache<Long, Optional<User>> cache = Caffeine.newBuilder()
            .maximumSize(10)
            .refreshAfterWrite(1, TimeUnit.MINUTES)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .build(key -> Optional.ofNullable(userMapper.selectByPrimaryKey(key)));

    public User getUserById(Long id) {
        try {
            Optional<User> cacheIfPresent = cache.getIfPresent(id);
            if (cacheIfPresent != null && cacheIfPresent.isPresent()) {
                return cacheIfPresent.get();
            }
            logger.error("未获取到缓存数据:{}", id);
            return null;
        } catch (Exception e) {
            logger.error("从缓存中获取数据异常", e);
            return null;
        }
    }
}
