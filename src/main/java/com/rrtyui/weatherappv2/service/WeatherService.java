package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dto.LocationSearchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;

    @Autowired
    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<LocationSearchDto> findAll(String city) {
        String url = UriComponentsBuilder
                .fromHttpUrl("https://api.openweathermap.org/geo/1.0/direct?")
                .queryParam("q", city)
                .queryParam("limit", 5)
                .queryParam("appid", "c868cca3f789b0d16792b9b8db92507c")
                .toUriString();

        ResponseEntity<LocationSearchDto[]> response = restTemplate.getForEntity(url, LocationSearchDto[].class);

        LocationSearchDto[] body = response.getBody();

        return List.of(body);
    }


}
