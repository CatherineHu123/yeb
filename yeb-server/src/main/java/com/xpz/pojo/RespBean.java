package com.xpz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 公共返回对象
 *
 * @Author: Catherine
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RespBean {
    //状态码
    private long code;
    //提示信息
    private String message;
    private Object object;

    /**
     * 成功返回
     */
    public static RespBean sucess(String message){
        return new RespBean(200, message, null);
    }

    /**
     * 成功返回
     */
    public static RespBean sucess(String message, Object o){
        return new RespBean(200, message, o);
    }

    /**
     * 失败返回
     */
    public static RespBean error(String message){
        return new RespBean(500, message, null);
    }

    /**
     * 失败返回
     */
    public static RespBean error(String message, Object o){
        return new RespBean(500, message, o);
    }
}
