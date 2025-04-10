package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.CustomSessionDao;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.InvalidSessionException;
import com.rrtyui.weatherappv2.util.mapper.MapperToCustomSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SessionService {
    private final CustomSessionDao customSessionDao;

    @Autowired
    public SessionService(CustomSessionDao customSessionDao) {
        this.customSessionDao = customSessionDao;
    }


    public CustomSession saveSessionToUser(User user) {
        CustomSession customSession = MapperToCustomSession.mapFrom(user);
        return customSessionDao.save(customSession);
    }

    public User getUserBySessionId(String sessionUuid) {
        Optional<CustomSession> customSession = findByUUID(sessionUuid);
        if (customSession.isEmpty()) {
            throw new InvalidSessionException("Invalid session: sign in / sign up");
        }
        return customSession.get().getUser();
    }

    private Optional<CustomSession> findByUUID(String uuid) {
        deleteInvalidSessions();
        return customSessionDao.findById(uuid);
    }

    private void deleteInvalidSessions() {
        List<CustomSession> customSessions = customSessionDao.findAll();
        for (CustomSession customSession : customSessions) {
            if (isValidTimeEnded(customSession.getExpiresAt())) {
                customSession.setUser(null);
                customSessionDao.delete(customSession);
            }
        }
    }

    private boolean isValidTimeEnded(LocalDateTime timeToCheck) {
        return LocalDateTime.now().isAfter(timeToCheck);
    }
}