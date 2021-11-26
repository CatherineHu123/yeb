package com.xpz.utils;

import com.xpz.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 操作员工具类
 *
 * @Author: Catherine
 */
public class AdminUtils {
    /**
     * 获取当前操作员
     */
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
