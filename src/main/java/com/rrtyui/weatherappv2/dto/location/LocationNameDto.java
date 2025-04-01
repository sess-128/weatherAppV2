package com.rrtyui.weatherappv2.dto.location;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LocationNameDto {

    @NotNull
    @Size(min = 3, max = 20, message = "Город от 3-х до 20-ти букв")
    private String city;
}
