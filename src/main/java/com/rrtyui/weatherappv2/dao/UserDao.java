package com.rrtyui.weatherappv2.dao;

import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.WrongCredentialsException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDao extends BaseDao<User> {
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional
    public Optional<User> findByLogin(User user) {
        try {
            User foundUser = (User) sessionFactory.getCurrentSession()
                    .createQuery("FROM User WHERE name = :login")
                    .setParameter("login", user.getName())
                    .getSingleResult();
            return Optional.ofNullable(foundUser);
        } catch (NoResultException e) {
            throw new WrongCredentialsException(e);
        }
    }
}
