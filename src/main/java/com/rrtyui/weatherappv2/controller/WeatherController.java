package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dto.location.LocationNameDto;
import com.rrtyui.weatherappv2.dto.location.LocationSaveDto;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.AuthService;
import com.rrtyui.weatherappv2.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/search-results")
public class WeatherController extends BaseController {
    protected WeatherController(AuthService authService, WeatherService weatherService) {
        super(authService, weatherService);
    }

    @GetMapping
    public String search(@ModelAttribute("search") @Valid LocationNameDto locationNameDto,
                         BindingResult bindingResult,
                         Model model) {
        User user = authService.getCurrentUser();
        model.addAttribute("user", user);

        if (bindingResult.hasErrors()) {
            model.addAttribute("valid", bindingResult);
            model.addAttribute("cities", Collections.emptyList());
            model.addAttribute("locationSave", new LocationSaveDto());
            return "search-results";
        }

        List<LocationSearchDto> cities = weatherService.findAll(locationNameDto);
        model.addAttribute("cities", cities);
        model.addAttribute("locationSave", new LocationSaveDto());

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
