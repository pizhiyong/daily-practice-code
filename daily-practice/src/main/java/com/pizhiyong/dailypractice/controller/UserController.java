package com.pizhiyong.dailypractice.controller;


import com.pizhiyong.dailypractice.entity.User;
import com.pizhiyong.dailypractice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pzy/demo")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/user/get")
    @ResponseBody
    public User getUserInfo(@RequestParam(name = "id") long id) {
        User user = userService.getUserInfo(id);
        return user;
    }
}
