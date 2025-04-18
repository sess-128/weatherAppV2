package com.rrtyui.weatherappv2.dto.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class LocationShowDto {

    @JsonProperty("temp_c")
    private final BigDecimal currentTemp;

    @JsonProperty("feelslike_c")
    private final BigDecimal feelsLike;

    @JsonProperty("name")
    private final String city;

    @JsonProperty("country")
    private final String country;

    @JsonProperty("text")
    private final String skyState;

    @JsonProperty("humidity")
    private final int humidity;

    @JsonProperty("icon")
    private final String icon;
}
