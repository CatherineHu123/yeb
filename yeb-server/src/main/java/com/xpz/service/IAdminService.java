package com.xpz.service;

import com.xpz.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xpz.pojo.Menu;
import com.xpz.pojo.RespBean;
import com.xpz.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 登录之后返回 token
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 根据用户名获取用户
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据用户查询角色
     */
    List<Role> getRoles(Integer adminId);

    /**
     * 获取所有操作员
     */
    List<Admin> getAllAdmins(String keywords);

    /**
     * 更新操作员角色
     */
    RespBean updateAdminRole(Integer id, int[] rids);

    /**
     * 更新当前用户密码
     */
    RespBean updatePwd(String oldPwd, String newPwd, int id);
}
