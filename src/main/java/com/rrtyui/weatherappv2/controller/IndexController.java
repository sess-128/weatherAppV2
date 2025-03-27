package com.rrtyui.weatherappv2.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rrtyui.weatherappv2.dto.location.LocationSearchDto;
import com.rrtyui.weatherappv2.dto.location.LocationShowDto;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.CookieService;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.UserService;
import com.rrtyui.weatherappv2.service.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/")
public class IndexController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;
    private final WeatherService weatherService;

    @Autowired
    public IndexController(UserService userService, SessionService sessionService, CookieService cookieService, WeatherService weatherService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public String index(HttpServletRequest httpServletRequest,
                        Model model) throws JsonProcessingException {
        String sessionIdFromCookies = SessionService.getSessionIdFromCookies(httpServletRequest);

        Optional<CustomSession> customSession = sessionService.findByUUID(sessionIdFromCookies);
        User user = customSession.get().getUser();

        model.addAttribute("user", user);
        model.addAttribute("city", new LocationSearchDto());

        List<LocationShowDto> locationsForUser = weatherService.getLocationsForUser(user);

        model.addAttribute("locations_for_show", locationsForUser);

        return "index";
    }

    @GetMapping("/logout")
    public String logout() {
        cookieService.delete();
        return "redirect:/";
    }
}
