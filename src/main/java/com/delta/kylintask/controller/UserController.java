package com.delta.kylintask.controller;

import com.delta.kylintask.commons.ServerResponse;
import com.delta.kylintask.dto.UserDto;
import com.delta.kylintask.entity.UserInfo;
import com.delta.kylintask.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Api(tags = "User登录相关接口", description = "提供User登录相关的 Rest API")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("login")
    public ServerResponse<UserInfo> login(UserDto userDto){
        return userService.login(userDto);
    }

    @PostMapping("logout")
    public ServerResponse<UserInfo> logout(UserDto userDto){
        return userService.logout(userDto);
    }
}
