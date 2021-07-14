package com.imooc.controller;

import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "redis测试接口", tags = {"redis测试相关接口"})
@RestController
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/set")
    public void set(@RequestParam String key, @RequestParam String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @GetMapping("/get")
    public String get(@RequestParam String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    @GetMapping("/delete")
    public String delete(@RequestParam String key) {
        redisTemplate.delete(key);
        return "deleted!";
    }
}
