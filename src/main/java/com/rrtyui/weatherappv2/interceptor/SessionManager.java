package com.rrtyui.weatherappv2.interceptor;

import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.SessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

public class SessionManager implements HandlerInterceptor {

    private final SessionService sessionService;

    public SessionManager(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        if (requestURI.equals("/sign-in") || requestURI.equals("/sign-up")) {
            return true;
        }

        String sessionId = SessionService.getSessionUuidFromCookies(request);

        boolean permission = checkPermission(sessionId);

        if (!permission) {
            response.sendRedirect("/sign-in");
            return false;
        }

        return true;
    }

    private boolean checkPermission(String sessionId) {
        if (sessionId == null) {
            return false;
        }
        Optional<CustomSession> sessionOpt = sessionService.findByUUID(sessionId);
        return sessionOpt.isPresent();
    }
}