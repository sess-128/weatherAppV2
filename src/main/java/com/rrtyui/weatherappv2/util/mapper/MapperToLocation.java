package com.rrtyui.weatherappv2.util.mapper;

import com.rrtyui.weatherappv2.dto.location.LocationSaveDto;
import com.rrtyui.weatherappv2.entity.Location;


public class MapperToLocation {

    public static Location mapFrom(LocationSaveDto object) {
        return Location.builder()
                .name(object.getName())
                .latitude(object.getLatitude())
                .longitude(object.getLongitude())
                .build();
    }
}
