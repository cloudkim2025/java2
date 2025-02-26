package com.example.feignclint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class FeignClintApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeignClintApplication.class, args);
    }

}
