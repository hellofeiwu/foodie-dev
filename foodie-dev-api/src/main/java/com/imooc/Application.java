package com.imooc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
//扫描mybatis通用mapper所在的包
@MapperScan("com.imooc.mapper")
//扫描所有包以及相关组件包
@ComponentScan(basePackages = {"com.imooc", "org.n3r.idworker"})
@EnableScheduling   // 开启定时任务
public class Application {
    public static void main(String[] args) {
        try {
            SpringApplication.run(Application.class, args);
        }catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
