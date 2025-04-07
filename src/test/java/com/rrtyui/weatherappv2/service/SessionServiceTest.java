package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.CustomSessionDao;
import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.InvalidSessionException;
import com.rrtyui.weatherappv2.util.CustomServiceTest;
import com.rrtyui.weatherappv2.util.mapper.MapperToCustomSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@CustomServiceTest
class SessionServiceTest {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserDao userDao;

    private CookieService cookieService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .name("test")
                .password("123")
                .build();
        userDao.save(testUser);

        cookieService = mock(CookieService.class);
    }

    @Test
    void addCustomSession_ShouldSaveAndReturnCustomSession_WhenUserExist() {
        CustomSession customSession = sessionService.addCustomSession(testUser);

        assertNotNull(customSession);
        assertEquals(testUser, customSession.getUser());
        assertNotNull(customSession.getId());

        LocalDateTime shouldExpires = LocalDateTime.now().plusHours(1);
        LocalDateTime customSessionExpires = customSession.getExpiresAt();

        assertTrue(customSessionExpires.isAfter(LocalDateTime.now()));
        assertTrue(customSessionExpires.isBefore(shouldExpires.plusSeconds(5)));
    }

    @Test
    void getCurrentUser_ShouldReturnUser_WhenSessionIdExist() {
        CustomSession customSession = sessionService.addCustomSession(testUser);
        String sessionUuid = customSession.getId().toString();
        when(cookieService.getSessionId(any())).thenReturn(sessionUuid);

        User user = sessionService.getCurrentUser(sessionUuid);

        assertNotNull(user);
        assertEquals(user, testUser);
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenSessionDoesntExist() {
        String uuid = UUID.randomUUID().toString();

        assertThrows(InvalidSessionException.class, () -> sessionService.getCurrentUser(uuid));
    }
}