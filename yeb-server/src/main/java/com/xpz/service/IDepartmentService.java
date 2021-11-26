package com.xpz.service;

import com.xpz.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xpz.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface IDepartmentService extends IService<Department> {

    /**
     * 获取所有部门
     */
    List<Department> getAllDepartments();

    /**
     * 添加部门
     */
    RespBean addDep(Department dep);

    /**
     * 删除部门
     */
    RespBean deleteDep(Integer id);
}
