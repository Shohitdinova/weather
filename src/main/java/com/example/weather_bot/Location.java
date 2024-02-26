package com.example.weather_bot;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Location {
    String name;
    String region;
    String country;
    String localtime;
}
