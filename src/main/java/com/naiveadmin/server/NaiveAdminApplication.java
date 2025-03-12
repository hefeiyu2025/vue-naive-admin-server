package com.naiveadmin.server;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Vue Naive Admin 后台服务启动类
 */
@SpringBootApplication
@EnableCaching
@MapperScan("com.naiveadmin.server.mapper")
public class NaiveAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(NaiveAdminApplication.class, args);
    }
} 