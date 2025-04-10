package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.UserAlreadyExistException;
import com.rrtyui.weatherappv2.util.CustomTest;
import com.rrtyui.weatherappv2.util.PasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@CustomTest
class UserServiceIT {
    @Autowired
    private UserService userService;

    private UserSaveDto userSaveDto;
    private UserLoginDto userLoginDto;

    @BeforeEach
    void setUp() {
        userSaveDto = new UserSaveDto();
        userSaveDto.setName("testUser");
        userSaveDto.setPassword("password123");

        userLoginDto = new UserLoginDto();
        userLoginDto.setName("testUser");
        userLoginDto.setPassword("password123");
    }

    @Test
    @Rollback
    void addUser_WhenUsernameAlreadyExists_ShouldThrowException() {
        userService.addUser(userSaveDto);

        assertThrows(UserAlreadyExistException.class, () -> userService.addUser(userSaveDto));
    }

    @Test
    @Rollback
    void addUser_WhenCredentialsNotOccupied_ShouldSaveUser() {
        User user = userService.addUser(userSaveDto);

        assertNotNull(user);
        assertEquals(user.getName(), userSaveDto.getName());
        assertTrue(PasswordEncoder.isCorrectPassword(userSaveDto.getPassword(), user.getPassword()));
        assertNotNull(user.getId());
    }

    @Test
    @Rollback
    void findByLogin_WhenExists_ShouldReturnUser() {
        User savedUser = userService.addUser(userSaveDto);

        Optional<User> actualUser = userService.findByLogin(userLoginDto);

        assertTrue(actualUser.isPresent());
        assertEquals(actualUser.get().getName(), savedUser.getName());
    }

    @Test
    @Rollback
    void findByLogin_WhenDoesntExist_ShouldReturnEmptyOpt() {
        Optional<User> actualUser = userService.findByLogin(userLoginDto);

        assertTrue(actualUser.isEmpty());
    }
}
