package com.rrtyui.weatherappv2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rrtyui.weatherappv2.dao.LocationDao;
import com.rrtyui.weatherappv2.dto.location.*;
import com.rrtyui.weatherappv2.entity.Location;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.ThirdPartyServiceException;
import com.rrtyui.weatherappv2.util.mapper.MapperToLocation;
import com.rrtyui.weatherappv2.util.mapper.MapperToLocationShowDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
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

    @Value("${api.key}")
    private String API_KEY;

    @Autowired
    public WeatherService(RestTemplate restTemplate, LocationDao locationDao, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.locationDao = locationDao;
        this.objectMapper = objectMapper;
    }

    public List<LocationSearchDto> findAll(LocationNameDto locationNameDto) {
        String url = UriComponentsBuilder
                .fromHttpUrl("http://api.weatherapi.com/v1/search.json")
                .queryParam("q", locationNameDto.getCity())
                .queryParam("key", API_KEY)
                .toUriString();

        try {
            ResponseEntity<LocationSearchDto[]> response = restTemplate.getForEntity(url, LocationSearchDto[].class);

            LocationSearchDto[] body = response.getBody();

            if (body == null) {
                return List.of();
            }
            return List.of(body);
        } catch (RestClientException e) {
            throw new ThirdPartyServiceException(e);
        }
    }

    public List<LocationShowDto> getLocationsForShow(User user) {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<LocationShowDto> locationForShow = new ArrayList<>();
        List<Location> locationsForUser = locationDao.getLocationsForUser(user);

        String url = UriComponentsBuilder
                .fromHttpUrl("http://api.weatherapi.com/v1/current.json")
                .queryParam("key", API_KEY)
                .queryParam("q", TO_REPLACE)
                .toUriString();

        fillLocationsForShow(locationsForUser, url, locationForShow);

        return locationForShow;
    }

    private void fillLocationsForShow(List<Location> locationsForUser, String url, List<LocationShowDto> locationForShow) {
        for (Location location : locationsForUser) {
            String latNlon = location.getLatitude().toString() + "," + location.getLongitude().toString();
            String replaced = url.replace(TO_REPLACE, latNlon);
            String json = restTemplate.getForObject(replaced, String.class);
            try {
                LocationByCoordinatesJson locationByCoordinatesJson = objectMapper.readValue(json, LocationByCoordinatesJson.class);
                LocationShowDto locationShowDto = MapperToLocationShowDto.mapFrom(locationByCoordinatesJson);

                locationForShow.add(locationShowDto);
            } catch (JsonProcessingException e) {
                throw new ThirdPartyServiceException(e);
            }
        }
    }

    public void saveLocationForCurrentUser(LocationSaveDto locationSaveDto, User user) {
        Location location = MapperToLocation.mapFrom(locationSaveDto);
        location.setUser(user);
        locationDao.save(location);
    }

    public void deleteLocation(User user, String city) {
        locationDao.delete(user, city);
    }
}
