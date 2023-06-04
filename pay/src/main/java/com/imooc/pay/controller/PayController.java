package com.imooc.pay.controller;

import com.imooc.pay.pojo.PayInfo;
import com.imooc.pay.service.IPayService;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Peng on 2023/5/21 22:13
 */
@Controller
@RequestMapping("/pay")
@Slf4j
public class PayController {
    @Autowired
    private IPayService iPayService;

    @Autowired
    private WxPayConfig wxPayConfig;

    //没有商户支付密钥，暂时用这个
    @GetMapping("/test")
    public ModelAndView createTest() {
        Map map = new HashMap<>();
        map.put("codeUrl","http://www.baidu.com");
        return new ModelAndView("createForWxNative",map);  //resources/templates/createForWxNative.ftl
    }

    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum) {

        PayResponse response = iPayService.create(orderId, amount, bestPayTypeEnum);
        Map<String, String> map = new HashMap<>();

        //支付方式不同，渲染不同，WXPAY_NATIVE使用codeUrl，ALIPAY_PC使用body
        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE) {
            map.put("codeUrl", response.getCodeUrl());
            map.put("orderId", orderId);
            map.put("returnUrl", wxPayConfig.getReturnUrl());
            return new ModelAndView("createForWxNative", map);  //resources/templates/createForWxNative.ftl
        } else if (bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC) {
            map.put("body", response.getBody());
            return new ModelAndView("createForAlipayPC", map);
        }
        throw new RuntimeException("暂不支持的支付类型");
    }

    @PostMapping("/notify")
    @ResponseBody
    public String asyncNotify(@RequestBody String notifyData) {
        return iPayService.asyncNotify(notifyData);
    }

    @GetMapping("/queryByOrderId")
    @ResponseBody
    public PayInfo queryByOrderId(@RequestParam("orderId") String orderId){
        log.info("查询支付记录...");
        return iPayService.queryByOrderId(orderId);
    }
}
