package com.imooc.mall.service;

import com.imooc.mall.pojo.User;
import com.imooc.mall.response.CommonReturnType;

/**
 * Created by Peng on 2023/5/29 17:35
 */
public interface UserService {
    /**
     * 注册
     * @param user
     * @return
     */
    CommonReturnType register(User user);

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    CommonReturnType login(String username, String password);
}
