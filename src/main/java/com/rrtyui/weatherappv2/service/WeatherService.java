package com.rrtyui.weatherappv2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrtyui.weatherappv2.dao.LocationDao;
import com.rrtyui.weatherappv2.dto.location.LocationByCoordinatesJson;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.dto.location.LocationShowDto;
import com.rrtyui.weatherappv2.entity.Location;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.mapper.MapperToLocationShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class WeatherService {
    private static final String TO_REPLACE = "shouldBeReplaced";
    private final RestTemplate restTemplate;
    private final LocationDao locationDao;
    private final ObjectMapper objectMapper;
    private final MapperToLocationShowDto mapperToLocationShowDto;

    @Value("${api.key}")
    private String API_KEY;

    @Autowired
    public WeatherService(RestTemplate restTemplate, LocationDao locationDao, ObjectMapper objectMapper, MapperToLocationShowDto mapperToLocationShowDto) {
        this.restTemplate = restTemplate;
        this.locationDao = locationDao;
        this.objectMapper = objectMapper;
        this.mapperToLocationShowDto = mapperToLocationShowDto;
    }

    public List<LocationSearchDto> findAll(String city) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://api.weatherapi.com/v1/search.json")
                .queryParam("q", city)
                .queryParam("key", API_KEY)
                .toUriString();

        ResponseEntity<LocationSearchDto[]> response = restTemplate.getForEntity(url, LocationSearchDto[].class);

        LocationSearchDto[] body = response.getBody();

        return List.of(body);
    }

    public List<LocationShowDto> getLocationsForUser(User user) throws JsonProcessingException {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<LocationShowDto> locationForShow = new ArrayList<>();
        List<Location> locationsForUser = locationDao.getLocationsForUser(user);

        String url = UriComponentsBuilder
                .fromHttpUrl("http://api.weatherapi.com/v1/current.json")
                .queryParam("key", API_KEY)
                .queryParam("q", TO_REPLACE)
                .toUriString();


        for (Location location : locationsForUser) {
            String latNlon = location.getLatitude().toString() + "," + location.getLongitude().toString();
            String replaced = url.replace(TO_REPLACE, latNlon);
            String json = restTemplate.getForObject(replaced, String.class);
            LocationByCoordinatesJson locationByCoordinatesJson = objectMapper.readValue(json, LocationByCoordinatesJson.class);

            LocationShowDto locationShowDto = mapperToLocationShowDto.mapFrom(locationByCoordinatesJson);

            locationForShow.add(locationShowDto);
        }

        return locationForShow;
    }

    public void saveLocation(Location location) {
        locationDao.save(location);
    }

    public void deleteLocation(User user, String city) {
        locationDao.delete(user, city);
    }
}
