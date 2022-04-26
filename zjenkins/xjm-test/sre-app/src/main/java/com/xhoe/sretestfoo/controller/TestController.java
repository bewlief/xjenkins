package com.xhoe.sretestfoo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * sre-test-foo/com.xhoe.sretestfoo.securitydemo.controller/TestController
 *
 * @Author: xjming
 * @Date: 2020/7/20 17:00
 * @Description:
 * @Version: 1.0
 */

@RestController
public class TestController {

    @GetMapping("/{name}")
    public String hello(@PathVariable String name) {
        String s = String.format("welcome, %s", name);
        System.out.println(s);
        return s;
    }
}
