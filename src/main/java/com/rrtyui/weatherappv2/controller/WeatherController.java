package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dto.location.LocationSaveDto;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.AuthService;
import com.rrtyui.weatherappv2.service.WeatherService;
import com.rrtyui.weatherappv2.util.mapper.MapperToLocation;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/search-results")
public class WeatherController extends BaseController{
    protected WeatherController(AuthService authService, WeatherService weatherService) {
        super(authService, weatherService);
    }

    @GetMapping
    public String search(@RequestParam(value = "city")
                             @Size(min = 3, max = 20) String city,
                         Model model) {
        List<LocationSearchDto> cities = weatherService.findAll(city);

        User user = authService.getCurrentUser();

        model.addAttribute("cities", cities);
        model.addAttribute("locationSave", new LocationSaveDto());
        model.addAttribute("user", user);

        return "search-results";
    }

    @PostMapping
    public String addLocation(@ModelAttribute("locationSave") LocationSaveDto locationSaveDto) {
        User user = authService.getCurrentUser();

        weatherService.saveLocationForCurrentUser(locationSaveDto, user);

        return "redirect:/";
    }

    @DeleteMapping
    public String deleteLocation(@RequestParam("toDelete") String city) {
        User user = authService.getCurrentUser();
        weatherService.deleteLocation(user, city);

        return "redirect:/";
    }
}
