package com.darker.blog.controller;


import com.darker.blog.common.BaseController;
import com.darker.blog.common.Result;
import io.swagger.annotations.Api;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "LoginController", tags = {"登录接口"})
public class LoginController extends BaseController {

    /**
     * 登录接口
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestParam(value = "username", required = false) String username,
                        @RequestParam(value = "password", required = false) String password) {
        Subject subject = getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        try {
            subject.login(token);
            return new Result(this.getToken());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(e);
        }
    }

    /**
     * 注销接口
     *
     * @return
     */
    @GetMapping(value = "/logout")
    public Result logout() {
        Subject subject = getSubject();
        subject.logout();
        return new Result();
    }
}
