package com.imooc.controller;

import com.imooc.utils.RedisOperator;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "redis测试接口", tags = {"redis测试相关接口"})
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public void set(@RequestParam String key, @RequestParam String value) {
        redisOperator.set(key, value);
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String key) {
        redisOperator.del(key);
        return "deleted!";
    }
}
