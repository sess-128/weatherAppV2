package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dto.location.LocationSaveDto;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.entity.Location;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.WeatherService;
import com.rrtyui.weatherappv2.util.mapper.MapperToLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search-results")
public class WeatherController {
    private final WeatherService weatherService;
    private final SessionService sessionService;
    private final MapperToLocation mapperToLocation;

    @Autowired
    public WeatherController(WeatherService weatherService, SessionService sessionService, MapperToLocation mapperToLocation) {
        this.weatherService = weatherService;
        this.sessionService = sessionService;
        this.mapperToLocation = mapperToLocation;
    }

    @GetMapping
    public String search(@RequestParam(value = "city") String city,
                         Model model) {
        List<LocationSearchDto> cities = weatherService.findAll(city);

        User user = sessionService.getCurrentUser();

        model.addAttribute("cities", cities);
        model.addAttribute("locationSave", new LocationSaveDto());
        model.addAttribute("user", user);

        return "search-results";
    }

    @PostMapping
    public String addLocation(@ModelAttribute("locationSave") LocationSaveDto locationSaveDto) {
        User user = sessionService.getCurrentUser();

        Location location = mapperToLocation.mapFrom(locationSaveDto);
        location.setUser(user);

        weatherService.saveLocation(location);

        return "redirect:/";
    }

    @DeleteMapping
    public String deleteLocation(@RequestParam("toDelete") String city) {
        User user = sessionService.getCurrentUser();
        weatherService.deleteLocation(user, city);

        return "redirect:/";
    }
}
