package com.xpz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xpz.pojo.MenuRole;
import com.xpz.mapper.MenuRoleMapper;
import com.xpz.pojo.RespBean;
import com.xpz.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    /**
     * 更新角色菜单
     */
    @Override
    @Transactional
    public RespBean updateMenuRole(Integer rid, Integer[] mids) {
        // mids为空，此时“更新”为“删除”
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        if(null == mids||0 == mids.length){
            return RespBean.sucess("更新成功");
        }
        // 插入，批量更新
        Integer result = menuRoleMapper.insertRecord(rid, mids);
        if(result == mids.length){
            return RespBean.sucess("更新成功");
        }
        return RespBean.error("更新失败");
    }
}
