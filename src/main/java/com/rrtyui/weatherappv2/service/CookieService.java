package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.entity.CustomSession;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    private static final String COOKIE_NAME = "session_id";
    private final HttpServletRequest httpServletRequest;
    private final HttpServletResponse httpServletResponse;

    @Autowired
    public CookieService(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
    }

    public void addSessionToCookie(CustomSession customSession) {
        Cookie cookie = new Cookie(COOKIE_NAME, customSession.getId().toString());
        cookie.setMaxAge(60 * 60);
        httpServletResponse.addCookie(cookie);
    }

    public void delete() {
        Cookie[] cookies = httpServletRequest.getCookies();
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                cookie.setMaxAge(0);
                httpServletResponse.addCookie(cookie);
            }
        }
    }

    public String getSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
