package com.evil.devreminder.service;

import com.evil.devreminder.domain.Weather;

public interface WeatherService {
    public Weather getWeatherFor(String city);
}
