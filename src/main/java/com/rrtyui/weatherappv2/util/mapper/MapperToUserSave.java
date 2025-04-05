package com.rrtyui.weatherappv2.util.mapper;

import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.UserPasswordDecodeEncodeUtil;
import org.springframework.stereotype.Component;

@Component
public class MapperToUserSave {

    public static User mapFrom(UserSaveDto object) {
        return User.builder()
                .name(object.getName())
                .password(
                        UserPasswordDecodeEncodeUtil.encodePassword(object.getPassword())
                )
                .build();
    }
}
