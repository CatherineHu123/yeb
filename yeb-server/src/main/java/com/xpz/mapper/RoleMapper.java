package com.xpz.mapper;

import com.xpz.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户查询角色
     */
    List<Role> getRoles(Integer adminId);
}
