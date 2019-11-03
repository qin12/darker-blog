package com.darker.blog.controller;

import com.darker.blog.annotation.SysLog;
import com.darker.blog.orm.entity.User;
import com.darker.blog.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Api(tags = "UserController")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    @SysLog(value = "获取用户信息",exceptionMessage = "获取用户信息失败")
    public User getUserInfo(String username){
        return userService.selectUserByUserName(username);
    }
    @ResponseBody
    @RequestMapping(value = "add",method = RequestMethod.POST)
    @SysLog(value = "添加用户",exceptionMessage = "添加与用户失败")
    public void addUser(@RequestBody User user){
        userService.insert(user);
    }

}
