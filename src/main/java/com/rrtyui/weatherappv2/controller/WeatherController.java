package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dao.LocationDao;
import com.rrtyui.weatherappv2.dto.LocationSaveDto;
import com.rrtyui.weatherappv2.dto.LocationSearchDto;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/search-results")
public class WeatherController {
    private final WeatherService weatherService;
    private final SessionService sessionService;
    private final LocationDao locationDao;

    @Autowired
    public WeatherController(WeatherService weatherService, SessionService sessionService, LocationDao locationDao) {
        this.weatherService = weatherService;
        this.sessionService = sessionService;
        this.locationDao = locationDao;
    }

    @GetMapping
    public String search(@RequestParam(value = "city") String city,
                         Model model) {
        List<LocationSearchDto> cities = weatherService.findAll(city);

        model.addAttribute("cities", cities);
        model.addAttribute("location", new LocationSaveDto());

        return "search-results";
    }
}
