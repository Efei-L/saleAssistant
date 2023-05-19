package com.ltx.saleassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SaleAssistantApplication {

    public static void main(String[] args) {
        SpringApplication.run(SaleAssistantApplication.class, args);
    }

}
