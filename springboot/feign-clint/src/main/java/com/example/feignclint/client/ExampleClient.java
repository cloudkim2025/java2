package com.example.feignclint.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exampleClient", url = "${feign-data.url")
public interface ExampleClient {

    @GetMapping("/api/data/{id}")
    String getData(@PathVariable("id") Long id);
}
