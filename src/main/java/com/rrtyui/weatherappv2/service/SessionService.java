package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.CustomSessionDao;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.InvalidSessionException;
import com.rrtyui.weatherappv2.util.mapper.MapperToCustomSession;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    private final CustomSessionDao customSessionDao;
    private final HttpServletRequest httpServletRequest;
    private final MapperToCustomSession mapperToCustomSession;

    @Autowired
    public SessionService(CustomSessionDao customSessionDao, HttpServletRequest httpServletRequest, MapperToCustomSession mapperToCustomSession) {
        this.customSessionDao = customSessionDao;
        this.httpServletRequest = httpServletRequest;
        this.mapperToCustomSession = mapperToCustomSession;
    }

    public CustomSession add(User user) {
        CustomSession customSession = mapperToCustomSession.mapFrom(user);
        return customSessionDao.save(customSession);
    }

    public User getCurrentUser() {
        String sessionUuid = getSessionUuidFromCookies(httpServletRequest);
        Optional<CustomSession> customSession = findByUUID(sessionUuid);
        if (customSession.isEmpty()) {
            throw new InvalidSessionException("Invalid session: sign in / sign up");
        }
        return customSession.get().getUser();
    }

    public Optional<CustomSession> findByUUID(String uuid) {
        deleteInvalidSessions();
        return customSessionDao.findById(uuid);
    }

    public void deleteInvalidSessions() {
        List<CustomSession> customSessions = customSessionDao.findAll();
        for (CustomSession customSession : customSessions) {
            if (isValidTimeEnded(customSession.getExpiresAt())) {
                customSession.setUser(null);
                customSessionDao.delete(customSession);
            }
        }
    }

    public static String getSessionUuidFromCookies(HttpServletRequest request) {
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

    private boolean isValidTimeEnded(LocalDateTime timeToCheck) {
        return LocalDateTime.now().isAfter(timeToCheck);
    }
}

//TODO: при удалении сессии удаляется и пользователь -> настроить каскадное удаление
