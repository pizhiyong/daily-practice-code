package com.pizhiyong.dailypractice.controller;

import com.pizhiyong.dailypractice.entity.User;
import com.pizhiyong.dailypractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/pzy/demo")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息（原始方式）
     */
    @GetMapping("/user/get")
    @ResponseBody
    public User getUserInfo(@RequestParam(name = "id") long id) {
        User user = userService.getUserInfo(id);
        return user;
    }
}
