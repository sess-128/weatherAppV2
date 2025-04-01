package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.service.AuthService;
import com.rrtyui.weatherappv2.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseController {
    protected final AuthService authService;
    protected final WeatherService weatherService;

    @Autowired
    protected BaseController(AuthService authService, WeatherService weatherService) {
        this.authService = authService;
        this.weatherService = weatherService;
    }
}
