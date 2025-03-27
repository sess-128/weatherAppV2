package com.rrtyui.weatherappv2.util.mapper;

public interface Mapper<F, T> {
    T mapFrom(F object);
}
