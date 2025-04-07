package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.util.mapper.MapperToCustomSession;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CookieServiceTest {

    private static final String COOKIE_NAME = "session_id";
    private HttpServletRequest request;
    private HttpServletResponse response;
    private CookieService cookieService;
    private CustomSession customSession;

    @BeforeEach
    void setUp() {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        cookieService = new CookieService(request, response);

        customSession = MapperToCustomSession.mapFrom(null);
    }

    @Test
    void addSessionToCookie_ShouldAddCookie_WhenSessionExist() {
        cookieService.addSessionToCookie(customSession);

        String sessionId = customSession.getId().toString();

        verify(response).addCookie(argThat(cookie ->
                COOKIE_NAME.equals(cookie.getName()) &&
                        sessionId.equals(cookie.getValue()) &&
                        cookie.getMaxAge() == 60 * 60
        ));
    }

    @Test
    void delete_ShouldSetCookieMaxAgeToZero_WhenSessionCookieExists() {
        Cookie testCookie = new Cookie(COOKIE_NAME, "test");
        Cookie[] cookies = {testCookie};
        when(request.getCookies()).thenReturn(cookies);

        cookieService.delete();

        verify(response).addCookie(argThat(cookie ->
                    COOKIE_NAME.equals(cookie.getName()) &&
                            cookie.getMaxAge() == 0
                ));
    }

    @Test
    void getSessionId_ShouldReturnSessionId_WhenCookieExists() {
        Cookie testCookie = new Cookie(COOKIE_NAME, "test");
        Cookie[] cookies = {testCookie};
        when(request.getCookies()).thenReturn(cookies);

        String sessionId = cookieService.getSessionId(request);

        assertEquals(sessionId, testCookie.getValue());
    }

    @Test
    void getSessionId_ShouldReturnNull_WhenCookieDoesntExist() {
        Cookie testCookie = new Cookie("another_value", "test");
        Cookie[] cookies = {testCookie};
        when(request.getCookies()).thenReturn(cookies);

        String sessionId = cookieService.getSessionId(request);

        assertNull(sessionId);
    }
}