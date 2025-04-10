package com.rrtyui.weatherappv2.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserLoginDto {
    @NotBlank
    @Size(min = 3, max = 10, message = "Логин длинной от 3 до 10 символов")
    private String name;

    @NotBlank
    @Size(min = 3, message = "Пароль длиной от 3-х символов")
    private String password;
}
