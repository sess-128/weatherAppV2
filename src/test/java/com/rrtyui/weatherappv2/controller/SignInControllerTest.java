package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.AbstractMvcTest;
import com.rrtyui.weatherappv2.util.CustomTest;
import com.rrtyui.weatherappv2.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomTest
class SignInControllerTest extends AbstractMvcTest {

    @Autowired
    private UserDao userDao;

    @BeforeEach
    void setUp() {
        User user = User.builder()
                .name("testUser")
                .password(PasswordEncoder.encodePassword("password"))
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
                        .param("name", "testUser")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    void signIn_ShouldReturnBackToSignIn_WhenPasswordWrong() throws Exception {
        mockMvc.perform(post("/sign-in")
                        .param("name", "testUser")
                        .param("password", "wrongPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"))
                .andExpect(model().attributeExists("wrongCredentials"));
    }

    @Test
    void signIn_ShouldReturnBackToSignIn_WhenLoginWrong() throws Exception {
        mockMvc.perform(post("/sign-in")
                        .param("name", "user")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"))
                .andExpect(model().attributeExists("wrongCredentials"));
    }

    @Test
    void signIn_shouldReturnBackToSignUp_whenValidationFails() throws Exception {
        mockMvc.perform(post("/sign-in")
                        .param("name", "")
                        .param("password", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-in"))
                .andExpect(model().attributeExists("valid"))
                .andExpect(model().attributeHasFieldErrors("user", "name", "password"));
    }
}