package com.pizhiyong.dailypractice.dao;

import com.pizhiyong.dailypractice.entity.TiFlashUser;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * TiFlash用户数据访问接口（模拟实现）
 * 返回TiFlashUser实体，结构与MySQL的User不同
 *
 * @author pizhiyong
 * @since 1.0
 */
@Repository
public class TiflashUserMapper {

    /**
     * 根据主键查询用户（TiFlash实现）
     */
    public TiFlashUser selectByPrimaryKey(Long id) {
        // 模拟TiFlash查询，返回TiFlashUser结构
        TiFlashUser user = new TiFlashUser();
        user.setUserId(id);
        user.setUserName("TiFlash-User-" + id);
        user.setUserAge(25);
        user.setGenderCode(1); // 1表示男性
        user.setCreateTime(LocalDateTime.now().minusDays(30));
        user.setUpdateTime(LocalDateTime.now());
        user.setStatus("ACTIVE");
        user.setExtInfo("{\"source\":\"tiflash\",\"version\":\"1.0\"}");
        return user;
    }

    /**
     * 查询所有用户（TiFlash实现）
     */
    public List<TiFlashUser> selectAll() {
        // 模拟TiFlash批量查询
        TiFlashUser user1 = new TiFlashUser();
        user1.setUserId(1L);
        user1.setUserName("TiFlash-User-1");
        user1.setUserAge(25);
        user1.setGenderCode(1); // 男性
        user1.setCreateTime(LocalDateTime.now().minusDays(30));
        user1.setUpdateTime(LocalDateTime.now());
        user1.setStatus("ACTIVE");
        user1.setExtInfo("{\"source\":\"tiflash\",\"department\":\"IT\"}");

        TiFlashUser user2 = new TiFlashUser();
        user2.setUserId(2L);
        user2.setUserName("TiFlash-User-2");
        user2.setUserAge(30);
        user2.setGenderCode(2); // 女性
        user2.setCreateTime(LocalDateTime.now().minusDays(20));
        user2.setUpdateTime(LocalDateTime.now());
        user2.setStatus("ACTIVE");
        user2.setExtInfo("{\"source\":\"tiflash\",\"department\":\"HR\"}");

        return Arrays.asList(user1, user2);
    }

    /**
     * 根据年龄查询用户（TiFlash实现）
     */
    public List<TiFlashUser> selectByAge(int age) {
        // 模拟TiFlash条件查询
        return selectAll().stream()
                .filter(user -> user.getUserAge() >= age)
                .toList();
    }

    /**
     * 统计用户数量（TiFlash实现）
     */
    public long countUsers() {
        // 模拟TiFlash聚合查询
        return 1000000L; // 模拟大数据量
    }

    /**
     * 模拟TiFlash查询异常的方法
     */
    public TiFlashUser selectByPrimaryKeyWithException(Long id) {
        // 模拟TiFlash查询异常
        throw new RuntimeException("TiFlash connection timeout: Unable to connect to TiFlash cluster");
    }

    /**
     * 模拟TiFlash批量查询异常的方法
     */
    public List<TiFlashUser> selectAllWithException() {
        // 模拟TiFlash查询异常
        throw new RuntimeException("TiFlash query failed: Table not found in TiFlash");
    }
}
