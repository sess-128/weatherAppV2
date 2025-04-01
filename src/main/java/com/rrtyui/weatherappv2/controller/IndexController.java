package com.rrtyui.weatherappv2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.dto.location.LocationShowDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.AuthService;
import com.rrtyui.weatherappv2.service.WeatherService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
public class IndexController extends BaseController {
    protected IndexController(AuthService authService, WeatherService weatherService) {
        super(authService, weatherService);
    }

    @GetMapping
    public String index(Model model) throws JsonProcessingException {
        User user = authService.getCurrentUser();
        List<LocationShowDto> locationsForShow = weatherService.getLocationsForShow(user);

        model.addAttribute("user", user);
        model.addAttribute("city", new LocationSearchDto());
        model.addAttribute("locations_for_show", locationsForShow);

        return "index";
    }

    @GetMapping("/logout")
    public String logout() {
        authService.logout();
        return "redirect:/";
    }
}
