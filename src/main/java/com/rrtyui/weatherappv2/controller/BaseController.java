package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.service.CookieService;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.UserService;
import com.rrtyui.weatherappv2.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public abstract class BaseController {
    protected final UserService userService;
    protected final SessionService sessionService;
    protected final CookieService cookieService;
    protected final WeatherService weatherService;

    @Autowired
    public BaseController(UserService userService, SessionService sessionService, CookieService cookieService, WeatherService weatherService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
        this.weatherService = weatherService;
    }
}
