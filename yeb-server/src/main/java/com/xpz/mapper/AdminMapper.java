package com.xpz.mapper;

import com.xpz.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xpz.pojo.Menu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface AdminMapper extends BaseMapper<Admin> {
    /**
     * 获取所有操作员
     */
    List<Admin> getAllAdmins(@Param("id")Integer id, @Param("keywords")String keywords);

}
