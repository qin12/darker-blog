package com.darker.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("test")
public class TestController {

    @ResponseBody
    @RequestMapping(value = "/hello",method = RequestMethod.GET)
    public String test(){
        return "Hello World !";
    }

    @ResponseBody
    @RequestMapping(value = "/good",method = RequestMethod.GET)
    public String good(){
        return "Hello World good !";
    }
}
