package com.example.feignclint.client;

import com.example.feignclint.dto.DataRequestDTO;
import com.example.feignclint.dto.DataResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "exampleClient", url = "${feign-data.url}")
public interface ExampleClient {

    // GET 요청 (데이터 조회)
    @GetMapping("/api/data/{id}")
    String getData(@PathVariable("id") Long id);

    @GetMapping("/api/data")
    List<DataResponseDTO> getDataAll();

    // POST 요청 (데이터 생성)
    @PostMapping("/api/data")
    String createData(@RequestBody DataRequestDTO dataRequestDTO);

    @PutMapping("/api/data/{id}")
    String updateData(@RequestBody DataRequestDTO dataRequestDTO, @PathVariable Long id);


    @DeleteMapping("/api/data/{id}")
    String deleteData(@PathVariable Long id);


}
