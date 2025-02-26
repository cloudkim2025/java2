package com.example.feigndate.Controller;

import com.example.feigndate.dto.DataRequestDTO;
import com.example.feigndate.dto.DataResponseDTO;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/data")
public class DataController {

    private Map<Long, DataResponseDTO> dataStore =new HashMap<>();
    private  Long idCnt = 1L;

    @PostConstruct
    public void initDataSource(){
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 1", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 2", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 3", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 4", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 5", 100));
        dataStore.put(idCnt++,new DataResponseDTO(1L,"Item 6", 100));
    }

    @GetMapping("/{id}")
    public DataResponseDTO getData(@PathVariable Long id){
        log.info("[Feign Data] select");
        return dataStore.get(id);
    }
    @PostMapping
    public DataResponseDTO createData(@RequestBody DataRequestDTO dataRequestDTO){
        log.info("/[Feign Data] create");
        DataResponseDTO newData = DataResponseDTO.builder()
                .id(idCnt)
                .name(dataRequestDTO.getName())
                .value(dataRequestDTO.getValue())
                .build();

        dataStore.put(idCnt++, newData);


        return newData;
    }

    @PutMapping("/{id}")
    public DataResponseDTO updateData(
            @PathVariable Long id,
            @RequestBody DataRequestDTO dataRequestDTO){
        log.info("/[Feign Data] update");
        DataResponseDTO dataResponseDTO = dataStore.get(id);

        dataResponseDTO.setName(dataRequestDTO.getName());
        dataResponseDTO.setValue(dataRequestDTO.getValue());
        dataStore.put(id, dataResponseDTO);

        return dataResponseDTO;
    }


}
