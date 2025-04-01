package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.dto.user.UserLoginDto;
import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.mapper.MapperToUserLogin;
import com.rrtyui.weatherappv2.util.mapper.MapperToUserSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;
    private final MapperToUserSave mapperToUserSave;
    private final MapperToUserLogin mapperToUserLogin;

    @Autowired
    public UserService(UserDao userDao, MapperToUserSave mapperToUserSave, MapperToUserLogin mapperToUserLogin) {
        this.userDao = userDao;
        this.mapperToUserSave = mapperToUserSave;
        this.mapperToUserLogin = mapperToUserLogin;
    }

    public User addUser(UserSaveDto userSaveDto) {
            User user = mapperToUserSave.mapFrom(userSaveDto);
            return userDao.save(user);
    }

    public Optional<User> findByLogin(UserLoginDto userLoginDto) {
        User user = mapperToUserLogin.mapFrom(userLoginDto);
        return userDao.findByLogin(user);
    }

}
