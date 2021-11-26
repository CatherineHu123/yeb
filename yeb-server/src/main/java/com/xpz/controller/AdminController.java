package com.xpz.controller;


import com.xpz.pojo.Admin;
import com.xpz.pojo.RespBean;
import com.xpz.pojo.Role;
import com.xpz.service.IAdminService;
import com.xpz.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
@RestController
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private IRoleService roleService;

    @ApiOperation(value = "获取所有操作员")
    @GetMapping("/getAllAdmins")
    public List<Admin> getAllAdmins(String keywords){
        return adminService.getAllAdmins(keywords);
    }

    @ApiOperation(value = "更新操作员")
    @PutMapping("/updateAdmin")
    public RespBean updateAdmin(@RequestBody Admin admin){
        if (adminService.updateById(admin)){
            return RespBean.sucess("成功更新操作员");
        }
        return RespBean.error("更新操作员失败");
    }

    @ApiOperation(value = "删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdmin(@PathVariable Integer id){
        if (adminService.removeById(id)){
            return RespBean.sucess("成功删除操作员");
        }
        return RespBean.error("删除操作员失败");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/getAllRoles")
    public List<Role> getAllRoles(){
        return roleService.list();
    }

    @ApiOperation(value = "更新操作员角色")
    @PutMapping("/updateAdminRole")
    public RespBean updateAdminRole(Integer id, int[] rids){
        return adminService.updateAdminRole(id, rids);
    }
}
