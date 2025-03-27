package com.rrtyui.weatherappv2.dto.location;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocationByCoordinatesJson {
    private Location location;
    private Current current;

    @Data
    public static class Location {
        private String name;
        private String country;
    }

    @Data
    public static class Current {
        private BigDecimal temp_c;
        private BigDecimal feelslike_c;
        private int humidity;
        private Condition condition;

        @Data
        public static class Condition {
            private String text;
            private String icon;
        }
    }


}
