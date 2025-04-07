package com.rrtyui.weatherappv2.util.mapper;

import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.PasswordEncoder;


public class MapperToUserLogin {

    public static User mapFrom(UserLoginDto object) {
        return User.builder()
                .name(object.getName())
                .password(
                        PasswordEncoder.encodePassword(object.getPassword())
                )
                .build();
    }
}
