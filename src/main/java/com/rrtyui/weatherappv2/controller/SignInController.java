package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.CookieService;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.service.UserService;
import com.rrtyui.weatherappv2.util.UserPasswordDecodeEncodeUtil;
import jakarta.validation.Valid;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/sign-in")
public class SignInController {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;

    @Autowired
    public SignInController(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    @GetMapping
    public String mainPage(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    @PostMapping
    public String signIn(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            return "sign-in";
        }

        Optional<User> savedUserOpt = userService.findByLogin(user);

        if (savedUserOpt.isEmpty()) {
            model.addAttribute("wrongCredentials", "Wrong password or login.");
            return "sign-in";
        }

        User savedUser = savedUserOpt.get();

        if (UserPasswordDecodeEncodeUtil.isCorrectPassword(user.getPassword(), savedUser.getPassword())) {
            model.addAttribute("wrongCredentials", "Wrong password or login.");
            return "sign-in";
        }

        CustomSession customSession = sessionService.add(savedUser);

        cookieService.addSessionToCookie(customSession);

        model.addAttribute(user);
        return "redirect:/";
    }
}
