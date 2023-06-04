package com.imooc.mall.service.impl;

import com.imooc.mall.dao.UserMapper;
import com.imooc.mall.enums.ResponseEnum;
import com.imooc.mall.pojo.User;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * Created by Peng on 2023/5/29 17:37
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public CommonReturnType register(User user) {
        //username不能重复
        int countByUsername = userMapper.countByUsername(user.getUsername());
        if (countByUsername > 0) {
            //throw new RuntimeException("用户名重复");
            return CommonReturnType.error(ResponseEnum.USERNAME_EXIST);
        }
        //email不能重复
        int countByEmail = userMapper.countByEmail(user.getEmail());
        if (countByEmail > 0) {
            //throw new RuntimeException("该Email已注册");
            return CommonReturnType.error(ResponseEnum.EMAIL_EXIST);
        }
        //MD5摘要算法(Spring自带)
        String password = DigestUtils.md5DigestAsHex(user.getPassword().getBytes(StandardCharsets.UTF_8));
        user.setPassword(password);
        //写入数据库
        int resultCount = userMapper.insertSelective(user);
        if (resultCount == 0) {
            //throw new RuntimeException("注册失败");
            CommonReturnType.error(ResponseEnum.ERROR);
        }

        return CommonReturnType.success();
    }

    @Override
    public CommonReturnType login(String username, String password) {
        User user = userMapper.selectByUsername(username);
        if (user == null) {
            //用户名或密码错误
            return CommonReturnType.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }
        String userInputPassword = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        if (!user.getPassword().equalsIgnoreCase(userInputPassword)) {  //这里需要忽略大小写
            //用户名或密码错误
            return CommonReturnType.error(ResponseEnum.USERNAME_OR_PASSWORD_ERROR);
        }

        user.setPassword("");  //密码前端不显示
        return CommonReturnType.success(user);
    }


}
