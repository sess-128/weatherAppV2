package com.rrtyui.weatherappv2.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rrtyui.weatherappv2.dao.LocationDao;
import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.dto.location.LocationNameDto;
import com.rrtyui.weatherappv2.dto.location.LocationSaveDto;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.dto.location.LocationShowDto;
import com.rrtyui.weatherappv2.entity.Location;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.CustomServiceTest;
import com.rrtyui.weatherappv2.util.mapper.MapperToLocation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@CustomServiceTest
class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private LocationDao locationDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private RestTemplate restTemplate;

    private LocationSearchDto locationSearchDto;

    private LocationNameDto locationNameDto;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("test")
                .password("123")
                .build();
        userDao.save(testUser);

        locationNameDto = new LocationNameDto();
        locationNameDto.setCity("Moscow");

        locationSearchDto = new LocationSearchDto();
        locationSearchDto.setCity("Moscow");
    }

    @Test
    void findAll_ShouldReturnLocations_WhenLocationsFound() {
        LocationSearchDto[] mockedResponse = {locationSearchDto};
        when(restTemplate.getForEntity(any(String.class), eq(LocationSearchDto[].class))).thenReturn(ResponseEntity.ok(mockedResponse));

        List<LocationSearchDto> result = weatherService.findAll(locationNameDto);

        assertEquals(1, result.size());
        assertEquals("Moscow", result.get(0).getCity());
    }

    @Test
    void findAll_ShouldReturnEmptyList_WhenNoneLocations() {
        LocationSearchDto[] mockedResponse = {};
        when(restTemplate.getForEntity(any(String.class), eq(LocationSearchDto[].class))).thenReturn(ResponseEntity.ok(mockedResponse));

        List<LocationSearchDto> result = weatherService.findAll(locationNameDto);

        assertEquals(0, result.size());
        verify(restTemplate, atLeastOnce())
                .getForEntity(any(String.class), eq(LocationSearchDto[].class));
    }

    @Test
    void getLocationsForShow_ShouldReturnCorrectDtoList() {
        Location location = new Location();
        location.setName("Moscow");
        location.setLatitude(BigDecimal.valueOf(55.7558));
        location.setLongitude(BigDecimal.valueOf(37.6173));
        location.setUser(testUser);
        locationDao.save(location);

        String fakeJsonResponse = """
                {
                  "location": {
                    "name": "Moscow",
                    "country": "Russia"
                  },
                  "current": {
                    "temp_c": 5.0,
                    "feelslike_c": 3.0,
                    "humidity": 80,
                    "condition": {
                      "text": "Cloudy",
                      "icon": "//cdn.weatherapi.com/weather/64x64/day/116.png"
                    }
                  }
                }
                """;

        when(restTemplate.getForObject(any(String.class), eq(String.class))).thenReturn(fakeJsonResponse);

        List<LocationShowDto> result = weatherService.getLocationsForShow(testUser);


        assertEquals(1, result.size());

        LocationShowDto dto = result.get(0);
        assertEquals("Moscow", dto.getCity());
        assertEquals(BigDecimal.valueOf(5.0), dto.getCurrentTemp());
    }

    @Test
    void saveLocationForCurrentUser_ShouldSaveLocationForUser() {
        LocationSaveDto locationSaveDto = new LocationSaveDto();
        locationSaveDto.setLatitude(BigDecimal.valueOf(11));
        locationSaveDto.setLongitude(BigDecimal.valueOf(11));
        locationSaveDto.setName("Moscow");

        weatherService.saveLocationForCurrentUser(locationSaveDto, testUser);

        List<Location> resultList = locationDao.getLocationsForUser(testUser);

        assertEquals(1, resultList.size());

        Location result = resultList.get(0);
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals("Moscow", result.getName());
        assertEquals(testUser, result.getUser());
    }

    @Test
    void deleteLocation_ShouldDeleteLocationForThisUser() {
        LocationSaveDto testMoscow = new LocationSaveDto();
        testMoscow.setLatitude(BigDecimal.valueOf(11));
        testMoscow.setLongitude(BigDecimal.valueOf(11));
        testMoscow.setName("Moscow");

        LocationSaveDto testLondon = new LocationSaveDto();
        testLondon.setLatitude(BigDecimal.valueOf(11));
        testLondon.setLongitude(BigDecimal.valueOf(11));
        testLondon.setName("London");

        weatherService.saveLocationForCurrentUser(testLondon, testUser);
        weatherService.saveLocationForCurrentUser(testMoscow, testUser);

        weatherService.deleteLocation(testUser, testLondon.getName());

        List<Location> resultList = locationDao.getLocationsForUser(testUser);

        assertEquals(1, resultList.size());

        Location result = resultList.get(0);
        assertEquals(testUser.getId(), result.getUser().getId());
        assertEquals(testMoscow.getName(), result.getName());
        assertEquals(testUser, result.getUser());
    }

    @Test
    void deleteLocation_ShouldNotDeleteLocationOfAnotherUser() {
        User anotherUser = User.builder()
                .name("anotherUser")
                .password("password")
                .build();
        userDao.save(anotherUser);

        LocationSaveDto locationDto = new LocationSaveDto();
        locationDto.setLatitude(BigDecimal.valueOf(10));
        locationDto.setLongitude(BigDecimal.valueOf(20));
        locationDto.setName("Paris");

        weatherService.saveLocationForCurrentUser(locationDto, anotherUser);

        weatherService.deleteLocation(testUser, "Paris");

        List<Location> anotherUserLocations = locationDao.getLocationsForUser(anotherUser);
        List<Location> testUserLocations = locationDao.getLocationsForUser(testUser);

        assertEquals(1, anotherUserLocations.size(), "Локация другого пользователя не должна быть удалена");
        assertEquals(0, testUserLocations.size(), "У текущего пользователя не должно быть локаций");
    }
}