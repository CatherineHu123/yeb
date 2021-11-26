package com.xpz.service;

import com.xpz.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xpz.pojo.RespBean;
import com.xpz.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
public interface IEmployeeService extends IService<Employee> {

    /**
     * 获取所有员工（分页）
     * @return
     */
    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    /**
     * 获取工号
     */
    RespBean maxWorkID();

    /**
     * 添加员工
     */
    RespBean addEmp(Employee employee);

    List<Employee> getEmployee(Integer id);

    /**
     * 获取员工工资套账
     * @param currentPage
     * @param size
     * @return
     */
    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);
}
