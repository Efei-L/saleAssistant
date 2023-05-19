package com.ltx.saleassistant.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class LearnCommandLineRunner implements CommandLineRunner {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Value("${my-setting.maxDialog}")
    private String maxDialog;
    @Override
    public void run(String... args) {
        stringRedisTemplate.opsForValue().set("rest:dialog",maxDialog);
        stringRedisTemplate.opsForValue().set("today:visit","0");
        System.out.println("设置默认dialog数量"+maxDialog+"...");
    }
}
