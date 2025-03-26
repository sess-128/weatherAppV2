package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.entity.User;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User addUser(User user) {
        String hashpw = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashpw);

        userDao.save(user);

        return user;
    }

    public Optional<User> findByLogin(User user) {
        return userDao.findByLogin(user);
    }
}
