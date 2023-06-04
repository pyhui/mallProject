package com.imooc.mall.controller;

import com.imooc.mall.form.UserLoginForm;
import com.imooc.mall.form.UserRegisterForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

/**
 * Created by Peng on 2023/5/30 14:27
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping("/register")
    public CommonReturnType register(@Valid @RequestBody UserRegisterForm userRegisterForm) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm, user);
        return userService.register(user);
    }

/*
    @PostMapping("/login")
    public CommonReturnType login(@RequestParam("username") String username,
                                  @RequestParam("password") String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return CommonReturnType.error(ResponseEnum.PARAM_ERROR,"用户名或密码不能为空");
        }
        CommonReturnType returnType = userService.login(username, password);
        //设置session
        httpServletRequest.getSession().setAttribute("currentUser", returnType.getData());
        return returnType;
    }
*/
    @PostMapping("/login")
    public CommonReturnType login(@Valid @RequestBody UserLoginForm userLoginForm) {
        CommonReturnType loginReturnType = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        //设置session
        httpServletRequest.getSession().setAttribute(CURRENT_USER, loginReturnType.getData());
        log.info("/user/login session={}", httpServletRequest.getSession().getId());
        return loginReturnType;
    }

    @GetMapping
    public CommonReturnType userInfo() {
        log.info("/user session={}", httpServletRequest.getSession().getId());
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        /*if (user == null) {
            return CommonReturnType.error(ResponseEnum.NEED_LOGIN);
        }*/
        return CommonReturnType.success(user);
    }

    //TODO 判断登陆状态，拦截器
    @PostMapping("/logout")
    public CommonReturnType logout() {
        log.info("/user/logout session={}", httpServletRequest.getSession().getId());
        /*User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        if (user == null) {
            return CommonReturnType.error(ResponseEnum.NEED_LOGIN);
        }*/
        //退出登录
        httpServletRequest.getSession().removeAttribute(CURRENT_USER);
        return CommonReturnType.success();
    }

}
