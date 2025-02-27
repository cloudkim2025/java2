package com.example.feignclint.dto;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DataRequestDTO {
    private String name;
    private int value;
}
