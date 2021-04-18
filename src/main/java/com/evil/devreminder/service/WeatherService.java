package com.evil.devreminder.service;

import com.evil.devreminder.domain.Weather;
import com.evil.devreminder.domain.WeatherForecast;

import java.util.List;

public interface WeatherService {
    public Weather getWeatherFor(String city);

    List<WeatherForecast> getWeatherForecastFor(double latitude, double longitude);
}
