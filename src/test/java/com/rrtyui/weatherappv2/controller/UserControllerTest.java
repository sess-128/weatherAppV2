package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dto.location.LocationShowDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.AuthService;
import com.rrtyui.weatherappv2.service.CookieService;
import com.rrtyui.weatherappv2.service.WeatherService;
import com.rrtyui.weatherappv2.util.AbstractMvcTest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerTest extends AbstractMvcTest {
    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private WeatherService weatherService;

    @MockitoBean
    private CookieService cookieService;

    @Test
    void index_ShouldAddAttributesAndReturnView_WhenUserSignIn() throws Exception {
        User testUser = new User();
        testUser.setName("Andrei");

        LocationShowDto locationDto = LocationShowDto.builder()
                .city("Moscow")
                .currentTemp(BigDecimal.valueOf(10))
                .build();

        when(cookieService.getSessionId(any(HttpServletRequest.class)))
                .thenReturn("valid-session-id");
        when(authService.getCurrentUser()).thenReturn(testUser);
        when(weatherService.getLocationsForShow(testUser)).thenReturn(List.of(locationDto));

        mockMvc.perform(get("/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("search"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("locations_for_show"));
    }

    @Test
    void index_ShouldRedirectToSignIn_WhenUserNotAuth() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign-in"));
    }

    @Test
    void delete_ShouldDeleteSessionAndCookie() throws Exception {
        when(cookieService.getSessionId(any(HttpServletRequest.class)))
                .thenReturn("valid-session-id");
        when(authService.getCurrentUser()).thenReturn(new User());

        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(authService).logout();

        when(cookieService.getSessionId(any(HttpServletRequest.class)))
                .thenReturn(null); // кука удалена

        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/sign-in"));
    }

}