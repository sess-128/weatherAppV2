package com.rrtyui.weatherappv2.dao;

import com.rrtyui.weatherappv2.entity.User;
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
    public Optional<User> findByName(User user) {
        try {
            User foundUser = (User) sessionFactory.getCurrentSession()
                    .createQuery("FROM User WHERE name = :name")
                    .setParameter("name", user.getName())
                    .getSingleResult();
            return Optional.of(foundUser);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
