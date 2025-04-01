package com.rrtyui.weatherappv2.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalAdvice {

    @ExceptionHandler(UserAlreadyExistException.class)
    public String handleUserAlreadyExist(Model model) {
        model.addAttribute("errorTitle", "Registration Error");
        model.addAttribute("errorDetails", "The username is already taken, please return to the main page and choose another one. Sorry for landing on this page");
        return "error";
    }

    @ExceptionHandler(WrongCredentialsException.class)
    public String handleWrongResult(Model model) {
        model.addAttribute("errorTitle", "Wrong credentials, please try again");
        model.addAttribute("errorDetails", "Wrong credentials, please try again or sign up as new user. Sorry for landing on this page");
        return "error";
    }

    @ExceptionHandler(ThirdPartyServiceException.class)
    public String handleAPIErrors(Model model) {
        model.addAttribute("errorTitle", "External service error");
        model.addAttribute("errorDetails", "We will try our best to speed up the resolution of problems on the external service side.");
        return "error";
    }
}