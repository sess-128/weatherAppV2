package com.rrtyui.weatherappv2.service;

import com.rrtyui.weatherappv2.dao.UserDao;
import com.rrtyui.weatherappv2.dto.user.UserSaveDto;
import com.rrtyui.weatherappv2.entity.User;
import com.rrtyui.weatherappv2.util.UserPasswordDecodeEncodeUtil;
import com.rrtyui.weatherappv2.util.mapper.MapperToUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;
    private final MapperToUser mapperToUser;

    @Autowired
    public UserService(UserDao userDao, MapperToUser mapperToUser) {
        this.userDao = userDao;
        this.mapperToUser = mapperToUser;
    }

    public User addUser(UserSaveDto userSaveDto) {
        User user = mapperToUser.mapFrom(userSaveDto);
        return userDao.save(user);
    }

    public Optional<User> findByLogin(User user) {
        return userDao.findByLogin(user);
    }
}
