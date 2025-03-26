package com.rrtyui.weatherappv2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrtyui.weatherappv2.dao.LocationDao;
import com.rrtyui.weatherappv2.dto.LocationSaveDto;
import com.rrtyui.weatherappv2.dto.LocationSearchDto;
import com.rrtyui.weatherappv2.dto.LocationShowDto;
import com.rrtyui.weatherappv2.dto.tees.Wwa;
import com.rrtyui.weatherappv2.entity.Location;
import com.rrtyui.weatherappv2.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class WeatherService {
    private final RestTemplate restTemplate;
    private final LocationDao locationDao;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherService(RestTemplate restTemplate, LocationDao locationDao, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.locationDao = locationDao;
        this.objectMapper = objectMapper;
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

    public List<LocationShowDto> getLocationsForUser(User user) throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        String url = UriComponentsBuilder
                .fromHttpUrl("http://api.weatherapi.com/v1/current.json")
                .queryParam("key", "e6ff48a146ab46a9a77133427252603")
                .queryParam("q", "shouldBeReplaced")
                .toUriString();

        List<LocationShowDto> locationForShow = new ArrayList<>();
        List<Location> locationsForUser = locationDao.getLocationsForUser(user);
        for (Location location : locationsForUser) {
            String latNlon = location.getLatitude().toString() + "," + location.getLongitude().toString();
            String replaced = url.replace("shouldBeReplaced", latNlon);
            String json = restTemplate.getForObject(replaced, String.class);
            Wwa wwa = objectMapper.readValue(json, Wwa.class);

            LocationShowDto locationShowDto = new LocationShowDto(
                    wwa.getCurrent().getTemp_c(),
                    wwa.getCurrent().getFeelslike_c(),
                    wwa.getLocation().getName(),
                    wwa.getLocation().getCountry(),
                    wwa.getCurrent().getCondition().getText(),
                    wwa.getCurrent().getHumidity(),
                    wwa.getCurrent().getCondition().getIcon()
            );

            locationForShow.add(locationShowDto);
        }

        return locationForShow;
    }

}
