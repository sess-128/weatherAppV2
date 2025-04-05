package com.rrtyui.weatherappv2.util.mapper;

import com.rrtyui.weatherappv2.entity.CustomSession;
import com.rrtyui.weatherappv2.entity.User;

import java.time.LocalDateTime;
import java.util.UUID;


public class MapperToCustomSession {

    public static CustomSession mapFrom(User object) {
        return CustomSession.builder()
                .id(UUID.randomUUID())
                .user(object)
                .expiresAt(LocalDateTime.now().plusHours(1))
                .build();
    }
}
