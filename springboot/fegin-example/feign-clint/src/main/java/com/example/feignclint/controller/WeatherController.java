package com.example.feignclint.controller;

import com.example.feignclint.dto.weather.WeatherResponse;
import com.example.feignclint.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weahter")
    public WeatherResponse getWeather() {
        return weatherService.getWeather();
    }

}
