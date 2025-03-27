package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dao.LocationDao;
import com.rrtyui.weatherappv2.dto.location.LocationSaveDto;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.Location;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
                         Model model,
                         HttpServletRequest httpServletRequest) {
        List<LocationSearchDto> cities = weatherService.findAll(city);

        String sessionIdFromCookies = SessionService.getSessionIdFromCookies(httpServletRequest);

        Optional<CustomSession> customSession = sessionService.findByUUID(sessionIdFromCookies);
        User user = customSession.get().getUser();

        model.addAttribute("cities", cities);
        model.addAttribute("locationSave", new LocationSaveDto());
        model.addAttribute("user", user);

        return "search-results";
    }

    @PostMapping
    public String addLocation(@ModelAttribute("locationSave") LocationSaveDto locationSaveDto,
                              HttpServletRequest httpServletRequest) {
        String sessionIdFromCookies = SessionService.getSessionIdFromCookies(httpServletRequest);

        Optional<CustomSession> customSession = sessionService.findByUUID(sessionIdFromCookies);
        User user = customSession.get().getUser();

        locationSaveDto.setUserId(user);

        Location location = new Location(
                null,
                locationSaveDto.getName(),
                user,
                locationSaveDto.getLatitude(),
                locationSaveDto.getLongitude()
        );

        locationDao.save(location);

        return "redirect:/";
    }

    @DeleteMapping
    public String deleteLocation(@RequestParam("toDelete") String city,
                                 HttpServletRequest httpServletRequest) {
        String sessionIdFromCookies = SessionService.getSessionIdFromCookies(httpServletRequest);

        Optional<CustomSession> customSession = sessionService.findByUUID(sessionIdFromCookies);
        User user = customSession.get().getUser();

        locationDao.delete(user, city);

        return "redirect:/";
    }
}
