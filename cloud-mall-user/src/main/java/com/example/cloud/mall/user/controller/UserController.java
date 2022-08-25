package com.example.cloud.mall.user.controller;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.cloud.mall.common.common.ApiRestResponse;
import com.example.cloud.mall.common.common.Constant;
import com.example.cloud.mall.common.exception.MallException;
import com.example.cloud.mall.common.exception.MallExceptionEnum;
import com.example.cloud.mall.common.util.EmailUtil;
import com.example.cloud.mall.user.model.entity.User;
import com.example.cloud.mall.user.service.EmailService;
import com.example.cloud.mall.user.service.UserService;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;

@RestController
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @GetMapping("/user")
    public User getUserInfo() {
        return userService.getUser();
    }

    @PostMapping("/adminRegister")
    public ApiRestResponse adminRegister(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }

        userService.adminRegister(username, password);

        return ApiRestResponse.success();
    }

    @PostMapping("/SimpleRegister")
    public ApiRestResponse SimpleRegister(@RequestParam("username") String username, @RequestParam("password") String password) {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }

        userService.SimpleRegister(username, password);

        return ApiRestResponse.success();
    }

    @GetMapping("/SimpleLogin")
    public ApiRestResponse SimpleLogin(@RequestParam("username") String username,
                                       @RequestParam("password") String password,
                                       HttpSession session) {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER, user);//session
        return ApiRestResponse.success(user);
    }

    @PostMapping("/register")
    public ApiRestResponse register(@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam("emailAddress") String emailAddress, @RequestParam("verificationCode") String verificationCode) {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }

        if (password.length() < 8) {
            return ApiRestResponse.error(MallExceptionEnum.PASSWORD_TOO_SHORT);
        }

        if (StringUtils.isNullOrEmpty(emailAddress)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_EMAIL_ADDRESS);
        }
        if (StringUtils.isNullOrEmpty(verificationCode)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_VERIFICATION_CODE);
        }
        boolean emailPassed = userService.checkEmailRegistered(emailAddress);
        if (!emailPassed) {
            return ApiRestResponse.error(MallExceptionEnum.EMAIL_HAS_BEEN_REGISTERED);
        }
        Boolean pass = emailService.checkEmailAndCode(emailAddress, verificationCode);
        if (!pass) {
            return ApiRestResponse.error(MallExceptionEnum.WRONG_VERIFICATION_CODE);
        }
        userService.register(username, password, emailAddress);

        return ApiRestResponse.success();
    }

    @GetMapping("/login")
    public ApiRestResponse login(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        user.setPassword(null);
        session.setAttribute(Constant.MALL_USER, user);//session
        return ApiRestResponse.success(user);
    }

    @PostMapping("/update")
    public ApiRestResponse updateUserInfo(HttpSession session, @RequestParam String signature) throws MallException {
        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        User user = new User();
        user.setId(currentUser.getId());
        user.setPersonalizedSignature(signature);
        userService.updateInformation(user);
        return ApiRestResponse.success();
    }

    @PostMapping("/logout")
    public ApiRestResponse logout(HttpSession session) {
        session.removeAttribute(Constant.MALL_USER);
        return ApiRestResponse.success();
    }

    @GetMapping("/adminLogin")
    public ApiRestResponse AdminLogin(@RequestParam("username") String username, @RequestParam("password") String password, HttpSession session) throws MallException {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        if (userService.checkAdminRole(user)) {
            //是管理员
            user.setPassword(null);
            session.setAttribute(Constant.MALL_USER, user);
            return ApiRestResponse.success(user);
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @PostMapping("/user/sendEmail")
    public ApiRestResponse sendEmail(@RequestParam("emailAddress") String emailAddress) {
        boolean validEmailAddress = EmailUtil.isValidEmailAddress(emailAddress);
        if (validEmailAddress) {
            boolean emailPassed = userService.checkEmailRegistered(emailAddress);
            if (!emailPassed) {
                return ApiRestResponse.error(MallExceptionEnum.EMAIL_HAS_BEEN_REGISTERED);
            } else {
                Boolean saveEmailToRedis = emailService.saveEmailToRedis(emailAddress, emailAddress);
                if (saveEmailToRedis) {
                    emailService.sendSimpleMessage(emailAddress, Constant.EMAIL_SUBJECT, "您的验证码是");
                    return ApiRestResponse.success();
                }
                return ApiRestResponse.error(MallExceptionEnum.EMAIL_HAS_BEEN_SEND);
            }
        } else {
            return ApiRestResponse.error(MallExceptionEnum.WRONG_EMAIL);
        }
    }

    @GetMapping("/loginWithJwt")
    public ApiRestResponse loginWithJwt(@RequestParam String username, String password) {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        user.setPassword(null);
        Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
        String token = JWT.create()
                .withClaim(Constant.USER_NAME, user.getUsername())
                .withClaim(Constant.USER_ID, user.getId())
                .withClaim(Constant.USER_ROLE, user.getRole())
                .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                .sign(algorithm);
        return ApiRestResponse.success(token);
    }

    @GetMapping("/adminLoginWithJwt")
    public ApiRestResponse adminLoginWithJwt(@RequestParam("username") String username, @RequestParam("password") String password) throws MallException {
        if (StringUtils.isNullOrEmpty(username)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_USER_NAME);
        }
        if (StringUtils.isNullOrEmpty(password)) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_PASSWORD);
        }
        User user = userService.login(username, password);
        if (userService.checkAdminRole(user)) {
            //是管理员
            user.setPassword(null);
            Algorithm algorithm = Algorithm.HMAC256(Constant.JWT_KEY);
            String token = JWT.create()
                    .withClaim(Constant.USER_NAME, user.getUsername())
                    .withClaim(Constant.USER_ID, user.getId())
                    .withClaim(Constant.USER_ROLE, user.getRole())
                    .withExpiresAt(new Date(System.currentTimeMillis() + Constant.EXPIRE_TIME))
                    .sign(algorithm);

            return ApiRestResponse.success(token);
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @PostMapping("/checkAdminRole")//feign
    public Boolean checkAdminRole(@RequestBody User user) {
        return userService.checkAdminRole(user);
    }

    @GetMapping("/getUser")//feign
    public User getUser(HttpSession session) {
        return (User) session.getAttribute(Constant.MALL_USER);
    }
}
