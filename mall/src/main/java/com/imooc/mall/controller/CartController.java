package com.imooc.mall.controller;

import com.imooc.mall.form.CartAddForm;
import com.imooc.mall.form.CartUpdateForm;
import com.imooc.mall.pojo.User;
import com.imooc.mall.response.CommonReturnType;
import com.imooc.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.imooc.mall.consts.MallConst.CURRENT_USER;

/**
 * Created by Peng on 2023/6/3 14:24
 */
@RestController
@RequestMapping("/carts")
public class CartController {
    @Autowired
    private CartService cartService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @GetMapping
    public CommonReturnType list() {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return cartService.list(user.getId());
    }

    @PostMapping
    public CommonReturnType add(@Valid @RequestBody CartAddForm cartAddForm) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return cartService.add(user.getId(), cartAddForm);
    }

    @PutMapping("/{productId}")
    public CommonReturnType update(@PathVariable Integer productId,
                                   @Valid @RequestBody CartUpdateForm cartUpdateForm) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return cartService.update(user.getId(), productId, cartUpdateForm);
    }

    @DeleteMapping("/{productId}")
    public CommonReturnType delete(@PathVariable Integer productId) {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return cartService.delete(user.getId(), productId);
    }

    @PutMapping("/selectAll")
    public CommonReturnType selectAll() {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    @PutMapping("/unSelectAll")
    public CommonReturnType unSelectAll() {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return cartService.unSelectAll(user.getId());
    }

    @GetMapping("/products/sum")
    public CommonReturnType sum() {
        User user = (User) httpServletRequest.getSession().getAttribute(CURRENT_USER);
        return cartService.sum(user.getId());
    }
}
