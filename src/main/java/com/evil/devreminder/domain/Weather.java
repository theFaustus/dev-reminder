package com.evil.devreminder.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class Weather {
    private String description;
    private double temperature;
    private double humidity;
    private String city;
    private String country;
}
