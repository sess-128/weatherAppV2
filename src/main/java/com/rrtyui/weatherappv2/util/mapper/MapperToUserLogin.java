package com.rrtyui.weatherappv2.util.mapper;

import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.UserPasswordDecodeEncodeUtil;
import org.springframework.stereotype.Component;

@Component
public class MapperToUserLogin implements Mapper<UserLoginDto, User> {
    @Override
    public User mapFrom(UserLoginDto object) {
        return User.builder()
                .name(object.getName())
                .password(
                        UserPasswordDecodeEncodeUtil.encodePassword(object.getPassword())
                )
                .build();
    }
}
