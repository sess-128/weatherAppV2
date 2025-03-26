package com.rrtyui.weatherappv2.dto;

import com.rrtyui.weatherappv2.entity.User;
import lombok.Data;

@Data
public class LocationDeleteDto {
    private String name;
    private User user;
}
