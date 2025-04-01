    package com.rrtyui.weatherappv2.controller;

    import com.rrtyui.weatherappv2.dto.user.AuthResult;
    import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
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
    @RequestMapping("/sign-in")
    public class SignInController extends BaseController{

        protected SignInController(AuthService authService, WeatherService weatherService) {
            super(authService, weatherService);
        }

        @GetMapping
        public String mainPage(Model model) {
            model.addAttribute("user", new UserLoginDto());
            return "sign-in";
        }

        @PostMapping
        public String signIn(@ModelAttribute("user") @Valid UserLoginDto userLoginDto,
                             BindingResult bindingResult,
                             Model model) {
            if (bindingResult.hasErrors()) {
                model.addAttribute("valid", bindingResult);
                return "sign-in";
            }

            AuthResult authResult = authService.authUser(userLoginDto);

            if (!authResult.passed()) {
                model.addAttribute("wrongCredentials", authResult.errorMessage());
                return "sign-in";
            }
            return "redirect:/";
        }
    }
