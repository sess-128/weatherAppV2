package com.rrtyui.weatherappv2.dao;

import com.rrtyui.weatherappv2.entity.Location;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

@Component
public class LocationDao extends BaseDao<Location> {
    public LocationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }
}
