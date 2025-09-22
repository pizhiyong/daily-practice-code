package com.pizhiyong.dailypractice.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Expiry;
import com.pizhiyong.dailypractice.dao.UserMapper;
import com.pizhiyong.dailypractice.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
            .expireAfter(new Expiry<Long, Optional<User>>() {
                @Override
                public long expireAfterCreate(Long key, Optional<User> value, long currentTime) {
                    return getNextExpiryNanos();
                }

                @Override
                public long expireAfterUpdate(Long key, Optional<User> value, long currentTime, long currentDuration) {
                    return getNextExpiryNanos();
                }

                @Override
                public long expireAfterRead(Long key, Optional<User> value, long currentTime, long currentDuration) {
                    return currentDuration;
                }
            })
            .build(key -> Optional.ofNullable(userMapper.selectByPrimaryKey(key)));

    private long getNextExpiryNanos() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextExpiry = now.withHour(4).withMinute(0).withSecond(0).withNano(0);
        if (now.compareTo(nextExpiry) >= 0) {
            nextExpiry = nextExpiry.plusDays(1);
        }
        // 添加最多 5 分钟的随机偏移，避免缓存雪崩
        long randomOffset = (long) (Math.random() * TimeUnit.MINUTES.toNanos(5));
        return ChronoUnit.NANOS.between(now, nextExpiry) + randomOffset;
    }

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
