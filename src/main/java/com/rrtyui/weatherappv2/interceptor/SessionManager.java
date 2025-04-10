package com.rrtyui.weatherappv2.interceptor;

import com.rrtyui.weatherappv2.service.CookieService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionManager implements HandlerInterceptor {
    private final CookieService cookieService;

    public SessionManager(CookieService cookieService) {
        this.cookieService = cookieService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/sign-in") || requestURI.equals("/sign-up")) {
            return true;
        }

        String sessionId = cookieService.getSessionId();

        boolean permission = checkPermission(sessionId);

        if (!permission) {
            response.sendRedirect("/sign-in");
            return false;
        }

        return true;
    }

    private boolean checkPermission(String sessionId) {
        return sessionId != null;
    }
}