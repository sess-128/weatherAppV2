package com.rrtyui.weatherappv2.dao;

import com.rrtyui.weatherappv2.entity.Location;
import com.rrtyui.weatherappv2.entity.User;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LocationDao extends BaseDao<Location> {
    public LocationDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional
    public List<Location> getLocationsForUser(User user) {
        return sessionFactory.getCurrentSession()
                .createQuery("SELECT l FROM Location l WHERE l.user = :user", Location.class)
                .setParameter("user", user)
                .getResultList();
    }

    @Transactional
    public void delete(User user, String city) {
        sessionFactory.getCurrentSession()
                .createQuery("DELETE FROM Location WHERE user = :user AND name = :city")
                .setParameter("user", user)
                .setParameter("city", city)
                .executeUpdate();
    }
}
