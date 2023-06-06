package com.imooc.mall.controller;

import com.imooc.mall.form.ShippingForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.ShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

/**
 * Created by Peng on 2023/6/6 17:47
 */
@RestController
@RequestMapping("/shippings")
public class ShippingController {
    @Autowired
    private ShippingService shippingService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @PostMapping
    public CommonReturnType add(@Valid @RequestBody ShippingForm form) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return shippingService.add(user.getId(), form);
    }

    @DeleteMapping("/{shippingId}")
    public CommonReturnType delete(@PathVariable Integer shippingId) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return shippingService.delete(user.getId(), shippingId);
    }

    @PutMapping("/{shippingId}")
    public CommonReturnType update(@PathVariable Integer shippingId,
                                   @Valid @RequestBody ShippingForm form) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return shippingService.update(user.getId(), shippingId, form);
    }

    @GetMapping
    public CommonReturnType list(@RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                 @RequestParam(required = false, defaultValue = "10") Integer pageSize) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return shippingService.list(user.getId(), pageNum, pageSize);
    }
}
