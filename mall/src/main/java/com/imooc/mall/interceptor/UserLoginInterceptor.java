package com.imooc.mall.interceptor;

import com.imooc.mall.exception.UserLoginException;
import com.imooc.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

/**
 * Created by Peng on 2023/6/1 16:15
 */
@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {
    /**
     * true 表示继续流程 false表示中断
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("preHandle...");
        request.getSession();
        User user = (User) request.getSession().getAttribute(CURRENT_USER);
        if (user == null) {
            log.info("user=null");
            throw new UserLoginException();
            //return false;
     //       return CommonReturnType.error(ResponseEnum.NEED_LOGIN);
        }
        return true;
    }
}
