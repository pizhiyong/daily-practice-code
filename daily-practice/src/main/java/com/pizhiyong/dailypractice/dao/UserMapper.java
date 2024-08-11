package com.pizhiyong.dailypractice.dao;

import com.pizhiyong.dailypractice.entity.User;

/**
* @author pizhiyong
* @description 针对表【user】的数据库操作Mapper
* @createDate 2024-04-14 22:46:57
* @Entity .entity.User
*/
public interface UserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

}
