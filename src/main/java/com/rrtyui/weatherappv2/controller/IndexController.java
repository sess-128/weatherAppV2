package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dto.LocationSearchDto;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.CookieService;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/")
public class IndexController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @Autowired
    public IndexController(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @GetMapping
    public String index(HttpServletRequest httpServletRequest,
                        Model model) {
        String sessionIdFromCookies = SessionService.getSessionIdFromCookies(httpServletRequest);

        Optional<CustomSession> customSession = sessionService.findByUUID(sessionIdFromCookies);
        User user = customSession.get().getUser();

        model.addAttribute("user", user);
        model.addAttribute("city", new LocationSearchDto());

        return "index";
    }

    @GetMapping("/logout")
    public String logout() {
        cookieService.delete();
        return "redirect:/";
    }
}
