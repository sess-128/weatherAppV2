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
                .fromHttpUrl("http://api.weatherapi.com/v1/search.json")
                .queryParam("q", city)
                .queryParam("key", "e6ff48a146ab46a9a77133427252603")
                .toUriString();

        ResponseEntity<LocationSearchDto[]> response = restTemplate.getForEntity(url, LocationSearchDto[].class);

        LocationSearchDto[] body = response.getBody();

        return List.of(body);
    }


}
