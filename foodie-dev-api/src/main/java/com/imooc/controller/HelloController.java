package com.imooc.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    final static Logger log = LoggerFactory.getLogger(HelloController.class);

    @GetMapping("/hello")
    public String hello() {
        log.debug("debug: hello~");
        log.info("info: hello~");
        log.warn("warn: hello~");
        log.error("error: hello~");

        return "hello world~";
    }
}
