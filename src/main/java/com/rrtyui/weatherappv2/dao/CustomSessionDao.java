package com.rrtyui.weatherappv2.dao;

import com.rrtyui.weatherappv2.entity.CustomSession;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class CustomSessionDao extends BaseDao<CustomSession> {
    public CustomSessionDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional
    public Optional<CustomSession> findById(String id) {
        Session session = sessionFactory.getCurrentSession();
        CustomSession customSession = session.get(CustomSession.class, UUID.fromString(id));
        return Optional.ofNullable(customSession);
    }

    @Transactional
    public List<CustomSession> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("FROM CustomSession ", CustomSession.class)
                .getResultList();
    }
}
