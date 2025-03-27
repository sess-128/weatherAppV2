package com.rrtyui.weatherappv2.dto.location;

import com.rrtyui.weatherappv2.entity.User;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class LocationSaveDto {
    private String name;
    private User userId;
    private BigDecimal latitude;
    private BigDecimal longitude;
}
