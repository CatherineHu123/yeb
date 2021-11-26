package com.xpz.controller;

import com.xpz.pojo.Admin;
import com.xpz.pojo.RespBean;
import com.xpz.service.IAdminService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Map;

/**
 * 更新当前用户、密码
 *
 * @Author: Catherine
 */
@RestController
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "更新当前用户")
    @PutMapping("/updateAdmin")
    public RespBean updateAdmin(@RequestBody Admin admin, Authentication authentication){
        if (adminService.updateById(admin)){
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(
                            admin, null, authentication.getAuthorities()
                    )
            );
            return RespBean.sucess("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "更新当前用户密码")
    @PutMapping("/updatePwd")
    public RespBean updatePwd(@RequestBody Map<String, Object> info){
        String oldPwd = ((String) info.get("oldPwd"));
        String newPwd = ((String) info.get("newPwd"));
        int id = (int) info.get("adminId");
        return adminService.updatePwd(oldPwd, newPwd, id);
    }
}
