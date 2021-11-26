package com.xpz.service;

import com.xpz.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xpz.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface IMenuRoleService extends IService<MenuRole> {

    /**
     * 更新角色菜单
     */
    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
