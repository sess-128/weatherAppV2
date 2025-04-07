package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.exception.UserAlreadyExistException;
import com.rrtyui.weatherappv2.util.mapper.MapperToUserLogin;
import com.rrtyui.weatherappv2.util.mapper.MapperToUserSave;
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

    public User addUser(UserSaveDto userSaveDto) {
        User user = MapperToUserSave.mapFrom(userSaveDto);
        if (userDao.findByName(user).isPresent()) {
            throw new UserAlreadyExistException();
        }
        return userDao.save(user);
    }

    public Optional<User> findByLogin(UserLoginDto userLoginDto) {
        User user = MapperToUserLogin.mapFrom(userLoginDto);
        return userDao.findByName(user);
    }
}
