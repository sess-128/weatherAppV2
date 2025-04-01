package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dto.user.AuthResult;
import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.UserPasswordDecodeEncodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final SessionService sessionService;
    private final CookieService cookieService;
    private static final String WRONG_CREDENTIALS = "Wrong password or login.";

    @Autowired
    public AuthService(UserService userService, SessionService sessionService, CookieService cookieService) {
        this.userService = userService;
        this.sessionService = sessionService;
        this.cookieService = cookieService;
    }

    public AuthResult authUser(UserLoginDto userLoginDto) {
        Optional<User> savedUserOpt = userService.findByLogin(userLoginDto);

        if (savedUserOpt.isEmpty()) {
            return AuthResult.failure(WRONG_CREDENTIALS);
        }

        User user = savedUserOpt.get();

        if (!UserPasswordDecodeEncodeUtil.isCorrectPassword(userLoginDto.getPassword(), user.getPassword())) {
            return AuthResult.failure("Wrong password or login.");
        }

        CustomSession customSession = sessionService.add(user);
        cookieService.addSessionToCookie(customSession);

        return AuthResult.complete();
    }

    public void saveUser(UserSaveDto userSaveDto) {
        User user = userService.addUser(userSaveDto);
        CustomSession customSession = sessionService.add(user);
        cookieService.addSessionToCookie(customSession);
    }

    public void logout() {
        cookieService.delete();
    }

    public User getCurrentUser() {
        return sessionService.getCurrentUser();
    }
}
