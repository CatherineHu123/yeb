package com.xpz.mapper;

import com.xpz.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xpz.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    /**
     * 获取所有部门
     */
    List<Department> getAllDepartments(int i);

    /**
     * 添加部门
     */
    void addDep(Department dep);

    /**
     * 删除部门
     */
    void deleteDep(Department dep);
}
