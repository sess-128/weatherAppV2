package com.rrtyui.weatherappv2.dao;

import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.User;
import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDao extends BaseDao<User> {
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Transactional
    public Optional<User> findByLogin(User user) {
        User foundUser = (User) sessionFactory.getCurrentSession()
                .createQuery("FROM User WHERE name = :login")
                .setParameter("login", user.getName())
                .getSingleResult();
        return Optional.ofNullable(foundUser);
    }
}
