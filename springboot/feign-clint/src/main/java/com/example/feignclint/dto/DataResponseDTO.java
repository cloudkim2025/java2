package com.example.feignclint.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataResponseDTO {
    private Long id;
    private String name;
    private int value;

}
