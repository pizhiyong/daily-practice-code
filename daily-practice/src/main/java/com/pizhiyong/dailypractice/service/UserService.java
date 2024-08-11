package com.pizhiyong.dailypractice.service;

import com.pizhiyong.dailypractice.dao.UserMapper;
import com.pizhiyong.dailypractice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public User getUserInfo(long id) {
        User user = userMapper.selectByPrimaryKey(id);
        return Optional.ofNullable(user).orElse(new User());
    }
}
