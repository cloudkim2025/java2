package com.example.weather.controller;

import com.example.weather.client.WeatherClient;
import com.example.weather.dto.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherClient weatherClient;

    @GetMapping
    public String showWeatherPage(Model model) {
        model.addAttribute("date" );
        model.addAttribute("time" );
        model.addAttribute("nx");
        model.addAttribute("ny");
        return "weatherAPI";
    }

    @GetMapping("/search")
    public String getWeather(@RequestParam String date,
                             @RequestParam String time,
                             @RequestParam int nx,
                             @RequestParam int ny,
                             Model model) {
        try {
            System.out.println("요청 데이터 → 날짜: " + date + ", 시간: " + time + ", nx: " + nx + ", ny: " + ny);

            WeatherResponseDto weatherResponse = weatherClient.getWeather(
                    "YcA8Np10sUqP575QlgVf+zog9ABR3OGnYGPTS3IZUZ2SzhXXBD4Tefvg9rMt2RSyvI+OqQ8SMSOdtWnTBdUo9g==",
                    10,
                    1,
                    "JSON",
                    date,
                    time,
                    nx,
                    ny
            );

            System.out.println("기상청 API 응답 → " + weatherResponse);

            if (weatherResponse == null || weatherResponse.getTemperature() == null) {
                System.err.println("데이터 없음: 응답이 null 또는 temperature 값이 없음.");
                model.addAttribute("error", "기상청에서 데이터를 가져올 수 없습니다.");
            } else {
                System.out.println("응답 데이터 바인딩 → 온도: " + weatherResponse.getTemperature());
                model.addAttribute("weather", weatherResponse.getTemperature());
            }

            model.addAttribute("date", date);
            model.addAttribute("time", time);
            model.addAttribute("nx", nx);
            model.addAttribute("ny", ny);
        } catch (Exception e) {
            System.err.println("API 요청 중 오류 발생: " + e.getMessage());
            model.addAttribute("error", "서버 오류가 발생했습니다.");
        }

        return "weatherAPI";
    }
}
