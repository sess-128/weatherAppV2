package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.SessionService;
import com.rrtyui.weatherappv2.util.AbstractMvcTest;
import com.rrtyui.weatherappv2.util.CustomTest;
import com.rrtyui.weatherappv2.util.PasswordEncoder;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomTest
class IndexControllerTest extends AbstractMvcTest {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SessionService sessionService;

    private CustomSession session;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("testUser")
                .password(PasswordEncoder.encodePassword("123"))
                .build();
        userDao.save(user);

        session = sessionService.saveSessionToUser(user);
    }

    @Test
    void index_shouldReturnIndexPageWithUserAndLocations() throws Exception {
        mockMvc.perform(get("/")
                        .cookie(new Cookie("session_id", session.getId().toString())))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("search"))
                .andExpect(model().attributeExists("locations_for_show"));
    }


    @Test
    void logout_shouldRedirectToRoot() throws Exception {
        mockMvc.perform(get("/logout")
                        .cookie(new Cookie("session_id", session.getId().toString())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }
}