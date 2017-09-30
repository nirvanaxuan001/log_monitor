package com.netease.ps.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by hzzhangxuan on 2017/9/25.
 */
@RestController
public class HelloWorldController {
    @RequestMapping("/hello")
    public String index(){
        System.out.println("aaa");
        return "Hello world";
    }
}
