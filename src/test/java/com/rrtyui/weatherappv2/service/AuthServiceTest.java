package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dto.user.AuthResult;
import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.InvalidSessionException;
import com.rrtyui.weatherappv2.util.CustomTest;
import com.rrtyui.weatherappv2.util.PasswordEncoder;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@CustomTest
class AuthServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private SessionService sessionService;

    @Mock
    private CookieService cookieService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private AuthService authService;

    private User testUser;
    private UserLoginDto userLoginDto;
    private UserSaveDto userSaveDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = User.builder()
                .id(1L)
                .name("testUser")
                .password("123")
                .build();

        userLoginDto = new UserLoginDto();
        userLoginDto.setName("testUser");
        userLoginDto.setPassword("123");

        userSaveDto = new UserSaveDto();
        userSaveDto.setName("testUser");
        userSaveDto.setPassword("123");
    }

    @Test
    void authUser_ShouldReturnSuccess_WhenCredentialsAreCorrect() {
        try (MockedStatic<PasswordEncoder> mockedPasswordEncoder = Mockito.mockStatic(PasswordEncoder.class)) {
            when(userService.findByLogin(userLoginDto)).thenReturn(Optional.of(testUser));
            when(sessionService.saveSessionToUser(testUser)).thenReturn(new CustomSession());

            mockedPasswordEncoder.when(() -> PasswordEncoder.isCorrectPassword(userLoginDto.getPassword(), testUser.getPassword())).thenReturn(true);

            AuthResult result = authService.authUser(userLoginDto);

            assertTrue(result.passed());
            verify(cookieService).addSessionToCookie(any(CustomSession.class));
        }
    }

    @Test
    void authUser_ShouldReturnFailure_WhenCredentialsAreIncorrect() {
        try (MockedStatic<PasswordEncoder> mockedPasswordEncoder = Mockito.mockStatic(PasswordEncoder.class)) {
            when(userService.findByLogin(userLoginDto)).thenReturn(Optional.of(testUser));
            when(sessionService.saveSessionToUser(testUser)).thenReturn(new CustomSession());

            mockedPasswordEncoder.when(() -> PasswordEncoder.isCorrectPassword(userLoginDto.getPassword(), testUser.getPassword())).thenReturn(false);

            AuthResult result = authService.authUser(userLoginDto);

            assertFalse(result.passed());
            assertEquals("Wrong password or login.", result.errorMessage());
        }
    }

    @Test
    void saveUser_ShouldCreateUserAndSessionAndCookie_WhenUserIsSaved() {
        when(userService.addUser(userSaveDto)).thenReturn(testUser);
        when(sessionService.saveSessionToUser(testUser)).thenReturn(new CustomSession());

        authService.saveUser(userSaveDto);

        verify(sessionService).saveSessionToUser(testUser);
        verify(cookieService).addSessionToCookie(any(CustomSession.class));
    }

    @Test
    void logout_ShouldDeleteCookie() {
        authService.logout();

        verify(cookieService).deleteCookieForSession();
    }

    @Test
    void getCurrentUser_ShouldReturnUser_WhenSessionIsValid() {
        String sessionId = "validSessionId";
        when(cookieService.getSessionId()).thenReturn(sessionId);
        CustomSession customSession = new CustomSession();
        customSession.setUser(testUser);
        when(sessionService.getUserBySessionId(sessionId)).thenReturn(testUser);

        User currentUser = authService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(testUser, currentUser);
    }

    @Test
    void getCurrentUser_ShouldThrowException_WhenSessionIsInvalid() {
        String sessionId = "invalidSessionId";
        when(cookieService.getSessionId()).thenReturn(sessionId);
        when(sessionService.getUserBySessionId(sessionId)).thenThrow(new InvalidSessionException("Invalid session"));

        assertThrows(InvalidSessionException.class, () -> authService.getCurrentUser());
    }
}