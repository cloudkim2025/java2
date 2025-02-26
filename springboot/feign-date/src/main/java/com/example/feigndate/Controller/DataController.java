package com.example.feigndate.Controller;

import com.example.feigndate.dto.DataResponseDTO;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/data")
public class DataController {
    private Map<Long, DataResponseDTO> dataStore =new HashMap<>();
    private  Long idCnt = 1L;

    @PostConstruct
    public void initDataSource(){
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 1", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 1", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 1", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 1", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 1", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 1", 100));
    }

    @GetMapping("/{id}")
    public DataResponseDTO getData(@PathVariable Long id){
        return dataStore.get(id);
    }
}
