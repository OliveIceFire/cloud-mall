package com.example.cloud.mall.user.service;


import com.example.cloud.mall.common.exception.MallException;
import com.example.cloud.mall.user.model.entity.User;

public interface UserService {
    User getUser();

    void register(String username, String password, String emailAddress) throws MallException;

    User login(String username, String password) throws MallException;

    void updateInformation(User user) throws MallException;

    boolean checkAdminRole(User user);

    boolean checkEmailRegistered(String emailAddress);

    void SimpleRegister(String username, String password);

    void adminRegister(String username, String password);
}
