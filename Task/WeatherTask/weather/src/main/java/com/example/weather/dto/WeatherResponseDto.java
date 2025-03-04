package com.example.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponseDto {

    @JsonProperty("response")
    private Response response;

    @Data
    public static class Response {
        @JsonProperty("body")
        private Body body;
    }

    @Data
    public static class Body {
        @JsonProperty("items")
        private Items items;
    }

    @Data
    public static class Items {
        @JsonProperty("item")
        private List<Item> item;
    }

    @Data
    public static class Item {
        @JsonProperty("category")
        private String category;

        @JsonProperty("fcstValue")
        private String fcstValue;

        @JsonProperty("fcstDate")
        private String fcstDate;

        @JsonProperty("fcstTime")
        private String fcstTime;
    }

    // 온도(TMP) 데이터 가져오기
    public String getTemperature() {
        if (response != null && response.body != null && response.body.items != null) {
            for (Item item : response.body.items.item) {
                if ("TMP".equals(item.category)) {
                    return item.fcstValue;
                }
            }
        }
        return null;
    }
}
