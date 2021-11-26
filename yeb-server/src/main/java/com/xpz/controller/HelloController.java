package com.xpz.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试Swagger2
 *
 * @Author: Catherine
 */
@RestController
public class HelloController {

    @GetMapping(value = "/hello")
    public String hello(){
        return "hello";
    }

    @GetMapping(value = "/employee/basic/hello")
    public String hello2() {return "/employee/basic/hello";}

    @GetMapping(value = "/employee/advanced/hello")
    public String hello3() {return "/employee/advanced/hello";}
}
