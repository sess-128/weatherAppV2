package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.dto.user.AuthResult;
import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.service.AuthService;
import com.rrtyui.weatherappv2.service.WeatherService;
import com.rrtyui.weatherappv2.util.AbstractMvcTest;
import com.rrtyui.weatherappv2.util.CustomServiceTest;
import com.rrtyui.weatherappv2.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomServiceTest
class SignInControllerTest extends AbstractMvcTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("test2")
                .password(PasswordEncoder.encodePassword("123"))
                .build();
        userDao.save(user);
    }

    @Test
    @Rollback
    void mainPage_ShouldReturnLoginPage() throws Exception {
        mockMvc.perform(get("/sign-in"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("sign-in"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("user"));
    }

    @Test
    void signIn_ShouldRedirectToHome_WhenCredentialsValid() throws Exception {
        mockMvc.perform(post("/sign-in")
                        .param("name", "test2")
                        .param("password", "123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

}