package com.example.cloud.mall.user.service.impl;


import com.example.cloud.mall.common.exception.MallException;
import com.example.cloud.mall.common.exception.MallExceptionEnum;
import com.example.cloud.mall.common.util.MD5Utils;
import com.example.cloud.mall.user.model.dao.UserMapper;
import com.example.cloud.mall.user.model.entity.User;
import com.example.cloud.mall.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserMapper userMapper;

    @Override
    public User getUser() {
        return userMapper.selectByPrimaryKey(1);
    }

    @Override
    public void register(String username, String password, String emailAddress) throws MallException {
        if (userMapper.selectByName(username) != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }

        User user = new User();
        user.setUsername(username);
        user.setEmailAddress(emailAddress);
        TurnToMd5ThenSavePassword(password, user);
    }

    @Override
    public User login(String username, String password) throws MallException {
        String md5Password;
        try {
            md5Password = MD5Utils.getMD5Str(password);
        } catch (Exception e) {
            throw new MallException(MallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        User user = userMapper.selectLogin(username, md5Password);
        if (user == null) {
            throw new MallException(MallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInformation(User user) throws MallException {
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {//2是管理员
        return user.getRole().equals(2);
    }

    @Override
    public boolean checkEmailRegistered(String emailAddress) {
        User user = userMapper.selectOneByEmailAddress(emailAddress);
        return user == null;
    }

    @Override
    public void SimpleRegister(String username, String password) {
        if (userMapper.selectByName(username) != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }

        User user = new User();
        user.setUsername(username);
        TurnToMd5ThenSavePassword(password, user);
    }

    @Override
    public void adminRegister(String username, String password) {
        if (userMapper.selectByName(username) != null) {
            throw new MallException(MallExceptionEnum.NAME_EXISTED);
        }

        User user = new User();
        user.setUsername(username);
        user.setRole(2);
        TurnToMd5ThenSavePassword(password, user);
    }

    private void TurnToMd5ThenSavePassword(String password, User user) {
        try {
            user.setPassword(MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            throw new MallException(MallExceptionEnum.PASSWORD_TOO_SHORT);
        }
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new MallException(MallExceptionEnum.INSERT_FAILED);
        }
    }
}
