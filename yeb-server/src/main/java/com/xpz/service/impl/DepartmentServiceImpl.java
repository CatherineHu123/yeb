package com.xpz.service.impl;

import com.xpz.pojo.Department;
import com.xpz.mapper.DepartmentMapper;
import com.xpz.pojo.RespBean;
import com.xpz.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

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
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    /**
     * 获取所有部门
     */
    @Override
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    /**
     * 添加部门
     */
    @Override
    public RespBean addDep(Department dep) {
        dep.setEnabled(true);
        departmentMapper.addDep(dep);
        if(1 == dep.getResult()){
            return RespBean.sucess("成功添加部门", dep);
        }
        return RespBean.error("添加部门失败");
    }

    /**
     * 删除部门
     */
    @Override
    public RespBean deleteDep(Integer id) {
        Department dep = new Department();
        dep.setId(id);
        departmentMapper.deleteDep(dep);
        if(-2 == dep.getResult()){
            return RespBean.error("该部门存在子部门，不允许删除");
        }
        if(-1 == dep.getResult()){
            return RespBean.error("该部门存在员工，不允许删除");
        }
        if(1 == dep.getResult()){
            return RespBean.sucess("删除部门成功");
        }
        return RespBean.error("删除部门失败");
    }
}
