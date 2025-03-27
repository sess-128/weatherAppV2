package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.CookieService;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/sign-up")
public class SignUpController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @Autowired
    public SignUpController(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("user", new UserSaveDto());
        return "sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute("user") @Valid UserSaveDto userSaveDto,
                         BindingResult bindingResult,
                         @RequestParam("repeatPassword") String repeatPassword,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "sign-up";
        }
        if (!userSaveDto.getPassword().equals(repeatPassword)) {
            model.addAttribute("passwordMismatchError", "Passwords do not match.");
            return "sign-up";
        }

        User user = userService.addUser(userSaveDto);

        CustomSession customSession = sessionService.add(user);

        cookieService.addSessionToCookie(customSession);

        model.addAttribute(userSaveDto);
        return "redirect:/";
    }
}
