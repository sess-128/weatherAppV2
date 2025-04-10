package com.rrtyui.weatherappv2.controller;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.AbstractMvcTest;
import com.rrtyui.weatherappv2.util.CustomTest;
import com.rrtyui.weatherappv2.util.PasswordEncoder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@CustomTest
class SignUpControllerTest extends AbstractMvcTest {

    @Autowired
    private UserDao userDao;

    @Test
    void mainPage_shouldReturnSignUpPageWithUserModel() throws Exception {
        mockMvc.perform(get("/sign-up"))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    void signUp_shouldRedirectToHome_whenDataIsValid() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("name", "newUser")
                        .param("password", "securePass123")
                        .param("repeatPassword", "securePass123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        User user = User.builder()
                .name("newUser")
                .password(PasswordEncoder.encodePassword("securePass123"))
                .build();
        User result = userDao.findByName(user).orElse(null);
        assertNotNull(result);
    }

    @Test
    void signUp_shouldReturnBackToSignUp_whenValidationFails() throws Exception {
        mockMvc.perform(post("/sign-up")
                        .param("name", "")
                        .param("password", "")
                        .param("repeatPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("sign-up"))
                .andExpect(model().attributeExists("valid"))
                .andExpect(model().attributeHasFieldErrors("user", "name", "password", "repeatPassword"));
    }
}
