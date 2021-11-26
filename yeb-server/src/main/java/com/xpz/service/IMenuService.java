package com.xpz.service;

import com.xpz.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xpz.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface IMenuService extends IService<Menu> {

    /**
     * 通过用户id获取菜单列表
     */
    List<Menu> getMeusByAdminId();

    /**
     * 根据角色获取菜单列表
     */
    List<Menu> getMenuWithRole();

    /**
     * 查询所有菜单
     */
    List<Menu> getAllMenus();
}
