package com.rrtyui.weatherappv2.dao;

import com.rrtyui.weatherappv2.exception.UserAlreadyExistException;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseDao<T> {
    protected final SessionFactory sessionFactory;

    @Autowired
    public BaseDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Transactional
    public T save(T entity) {
        try {
            Session session = sessionFactory.getCurrentSession();
            session.persist(entity);

            return entity;
        } catch (RuntimeException e) {
            throw new UserAlreadyExistException(e.getMessage());
        }
    }

    @Transactional
    public void delete(T entity) {
        Session session = sessionFactory.getCurrentSession();
        session.remove(entity);
    }
}
