package com.xpz.mapper;

import com.xpz.pojo.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    /**
     * 更新操作员角色
     */
    Integer addAdminRole(Integer id, int[] rids);
}
