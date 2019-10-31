package com.darker.blog.controller;

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
@Api(tags = "UserController", description = "商品品牌管理")
public class UserController {

    @Autowired
    UserService userService;

    @ResponseBody
    @RequestMapping(value = "getUser",method = RequestMethod.GET)
    public User getUserInfo(String username){
        return userService.selectUserByUserName(username);
    }
    @ResponseBody
    @RequestMapping(value = "add",method = RequestMethod.POST)
    public void addUser(@RequestBody User user){
        userService.insert(user);
    }

}
