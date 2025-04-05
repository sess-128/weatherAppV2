package com.rrtyui.weatherappv2.util.mapper;

import com.rrtyui.weatherappv2.dto.location.LocationByCoordinatesJson;
import com.rrtyui.weatherappv2.dto.location.LocationShowDto;
import org.springframework.stereotype.Component;

@Component
public class MapperToLocationShowDto {

    public static LocationShowDto mapFrom(LocationByCoordinatesJson object) {
        return LocationShowDto.builder()
                .currentTemp(object.getCurrent().getTemp_c())
                .feelsLike(object.getCurrent().getFeelslike_c())
                .city(object.getLocation().getName())
                .country(object.getLocation().getCountry())
                .skyState(object.getCurrent().getCondition().getText())
                .humidity(object.getCurrent().getHumidity())
                .icon(object.getCurrent().getCondition().getIcon())
                .build();
    }
}
