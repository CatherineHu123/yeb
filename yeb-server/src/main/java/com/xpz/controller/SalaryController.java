package com.xpz.controller;


import com.baomidou.mybatisplus.extension.api.R;
import com.xpz.pojo.RespBean;
import com.xpz.pojo.RespPageBean;
import com.xpz.pojo.Salary;
import com.xpz.service.IEmployeeService;
import com.xpz.service.ISalaryService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
@RestController
@RequestMapping("/salary/sob")
public class SalaryController {

    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "添加工资表")
    @PutMapping("/")
    private RespBean addSalary(@RequestBody Salary salary){
        salary.setCreateDate(LocalDateTime.now());
        if (salaryService.save(salary)){
            return RespBean.sucess("添加成功");
        }
        return RespBean.error("增加失败");
    }

    @ApiOperation(value = "删除工资表")
    @DeleteMapping("/{id}")
    private RespBean deleteSalary(@PathVariable int id){
        if (salaryService.removeById(id)){
            return RespBean.sucess("删除成功");
        }
        return RespBean.error("删除失败");
    }

    @ApiOperation(value = "更新工资表")
    @PostMapping("/")
    private RespBean updateSalary(@RequestBody Salary salary){
        if (salaryService.updateById(salary)){
            return RespBean.sucess("更新成功");
        }
        return RespBean.error("更新失败");
    }

    @ApiOperation(value = "获取全部工资")
    @GetMapping("/")
    private List<Salary> getAllSalary(){
        return salaryService.list();
    }

    @ApiOperation(value = "获取员工工资账套")
    @GetMapping("/getEmployeeWithSalary")
    public RespPageBean getEmployeeWithSalary(@RequestParam(defaultValue = "1") Integer currentPage,
                                              @RequestParam(defaultValue = "10") Integer size){
        return employeeService.getEmployeeWithSalary(currentPage, size);
    }
}
