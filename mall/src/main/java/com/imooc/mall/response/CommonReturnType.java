package com.imooc.mall.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.imooc.mall.enums.ResponseEnum;
import lombok.Data;

/**
 * Created by Peng on 2023/5/30 17:16
 */
@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class CommonReturnType {
    private Integer status;

    private String msg;

    private Object data;

    public CommonReturnType(Integer status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public CommonReturnType(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }

    public static CommonReturnType successByMsg(String msg) {
        return new CommonReturnType(ResponseEnum.SUCCESS.getCode(), msg);
    }

    public static CommonReturnType success() {
        return new CommonReturnType(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getDesc());
    }

    public static CommonReturnType success(Object data) {
        return new CommonReturnType(ResponseEnum.SUCCESS.getCode(), data);
    }

    public static CommonReturnType error(ResponseEnum responseEnum) {
        return new CommonReturnType(responseEnum.getCode(), responseEnum.getDesc());
    }

    public static CommonReturnType error(ResponseEnum responseEnum, String msg) {
        return new CommonReturnType(responseEnum.getCode(), msg);
    }

}
