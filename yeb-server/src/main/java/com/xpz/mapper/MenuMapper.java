package com.xpz.mapper;

import com.xpz.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xpz.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id获取用户列表
     */
    List<Menu> getMeusByAdminId(Integer id);

    /**
     * 根据角色获取菜单列表
     */
    List<Menu> getMenuWithRole();

    /**
     * 查询所有菜单
     */
    List<Menu> getAllMenus();
}
