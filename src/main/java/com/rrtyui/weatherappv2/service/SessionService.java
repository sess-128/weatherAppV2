package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.CustomSessionDao;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService {
    private final CustomSessionDao customSessionDao;

    @Autowired
    public SessionService(CustomSessionDao customSessionDao) {
        this.customSessionDao = customSessionDao;
    }

    public CustomSession add(User user) {
        CustomSession customSession = CustomSession.builder()
                .id(UUID.randomUUID())
                .user(user)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();

        return customSessionDao.save(customSession);
    }

    public Optional<CustomSession> findByUUID(String uuid) {
        deleteOldSessions();
        return customSessionDao.findById(uuid);
    }

    public static String getSessionIdFromCookies(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("session_id".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public void deleteOldSessions() {
        List<CustomSession> customSessions = customSessionDao.findAll();
        for (CustomSession customSession : customSessions) {
            if (isValidTimeEnded(customSession.getExpiresAt())) {
                customSessionDao.delete(customSession);
            }
        }
    }

    private boolean isValidTimeEnded (LocalDateTime timeToCheck) {
        return LocalDateTime.now().isAfter(timeToCheck);
    }
}
