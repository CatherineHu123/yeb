package com.xpz.service.impl;

import com.xpz.pojo.Admin;
import com.xpz.pojo.Menu;
import com.xpz.mapper.MenuMapper;
import com.xpz.pojo.Role;
import com.xpz.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {
    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据用户id获取用户列表
     */
    @Override
    public List<Menu> getMeusByAdminId() {
        Integer adminId = ((Admin)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
        //从缓存中获取菜单列表
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_"+adminId);
        if(CollectionUtils.isEmpty(menus)){
            //通过数据库查询
            menus = menuMapper.getMeusByAdminId(adminId);
            //将数据设置到redis里面去
            valueOperations.set("menu_"+adminId, menus);
        }
        return menus;
    }

    /**
     * 根据角色获取菜单列表
     */
    @Override
    public List<Menu> getMenuWithRole() {
        return menuMapper.getMenuWithRole();
    }

    /**
     * 查询所有菜单
     */
    @Override
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
