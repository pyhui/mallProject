package com.imooc.mall.controller;

import com.imooc.mall.form.OrderCreateForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

/**
 * Created by Peng on 2023/6/9 17:10
 */
@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping
    public CommonReturnType create(@Valid @RequestBody OrderCreateForm orderCreateForm) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return orderService.create(user.getId(), orderCreateForm.getShippingId());
    }

    @GetMapping
    public CommonReturnType list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return orderService.list(user.getId(), pageNum, pageSize);
    }

    @GetMapping("/{orderNo}")
    public CommonReturnType detail(@PathVariable Long orderNo) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return orderService.detail(user.getId(), orderNo);
    }

    @PutMapping("/{orderNo}")
    public CommonReturnType cancel(@PathVariable Long orderNo) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return orderService.cancel(user.getId(), orderNo);
    }
}
