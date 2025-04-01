package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.service.AuthService;
import com.rrtyui.weatherappv2.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sign-up")
public class SignUpController extends BaseController{
    protected SignUpController(AuthService authService, WeatherService weatherService) {
        super(authService, weatherService);
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("user", new UserSaveDto());
        return "sign-up";
    }

    @PostMapping
    public String signUp(@ModelAttribute("user") @Valid UserSaveDto userSaveDto,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("valid", bindingResult);
            return "sign-up";
        }

        authService.saveUser(userSaveDto);

        return "redirect:/";
    }
}
