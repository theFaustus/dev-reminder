package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WeatherForecast {
    private String timezone;
    private LocalDateTime date;
    private double dayTemperature;
    private double nightTemperature;
    private String description;

}
