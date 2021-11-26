package com.xpz.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.imports.ExcelImportService;
import com.baomidou.mybatisplus.extension.api.R;
import com.xpz.pojo.*;
import com.xpz.service.*;
import io.swagger.annotations.ApiOperation;
import net.bytebuddy.dynamic.scaffold.inline.RebaseDynamicTypeBuilder;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.rmi.server.ExportException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Catherine
 * @since 2021-09-28
 */
@RestController
@RequestMapping("/employee/basic")
public class EmployeeController {

    @Autowired
    private IEmployeeService employeeService;
    @Autowired
    private INationService nationService;
    @Autowired
    private IPoliticsStatusService politicsStatusService;
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IJoblevelService joblevelService;
    @Autowired
    private IPositionService positionService;
    /**
     * 获取所有员工（分页）
     */
    @ApiOperation(value = "获取所有员工（分页）")
    @GetMapping("/getAllEmployee")
    public RespPageBean getEmployeeByPage(@RequestParam(defaultValue = "1") Integer currentPage,
                                          @RequestParam(defaultValue = "10") Integer size,
                                          Employee employee,
                                          LocalDate[] beginDateScope){
        return employeeService.getEmployeeByPage(currentPage, size, employee, beginDateScope);
    }

    @ApiOperation(value = "获取所有民族")
    @GetMapping("/getAllNations")
    public List<Nation> getAllNations(){
        return nationService.list();
    }

    @ApiOperation(value = "获取所有政治面貌")
    @GetMapping("/getAllPoliticsStatus")
    public List<PoliticsStatus> getAllPoliticsStatus(){
        return politicsStatusService.list();
    }

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/getAllDepartment")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartments();
    }

    @ApiOperation(value = "获取所有职称")
    @GetMapping("/getAllJoblevel")
    public List<Joblevel> getAllJoblevel(){
        return joblevelService.list();
    }

    @ApiOperation(value = "获取所有职位")
    @GetMapping("/getAllPosition")
    public List<Position> getAllPosition(){
        return positionService.list();
    }

    @ApiOperation(value = "获取工号")
    @GetMapping("/maxWorkID")
    public RespBean maxWorkID(){
        return employeeService.maxWorkID();
    }

    /**
     * 添加员工
     */
    @ApiOperation(value = "添加员工")
    @PutMapping
    public RespBean addEmp(@RequestBody Employee employee){
        return employeeService.addEmp(employee);
    }

    /**
     * 更新员工
     */
    @ApiOperation(value = "更新员工")
    @PutMapping("/updateEmp")
    public RespBean updateEmp(@RequestBody Employee employee){
        if (employeeService.updateById(employee)){
            return RespBean.sucess("成功更新员工");
        }
        return RespBean.error("更新员工失败");
    }

    /**
     * 删除员工
     */
    @ApiOperation(value = "删除员工")
    @DeleteMapping("/deleteEmp/{id}")
    public RespBean deleteEmp(@PathVariable Integer id){
        if (employeeService.removeById(id)){
            return RespBean.sucess("成功删除员工");
        }
        return RespBean.error("删除员工失败");
    }

    /**
     * 导出员工表
     */
    @ApiOperation(value = "导出员工表")
    @GetMapping(value = "/exportEmployee", produces = "application/octet-stream")
    public void exportEmloyee(HttpServletResponse response) throws IOException {
        List<Employee> list = employeeService.getEmployee(null);
        ExportParams params = new ExportParams("员工表","员工表", ExcelType.HSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, Employee.class, list);
        // 使用流的形式
        response.setHeader("content-type", "application/octet-stream");
        // 防止中文乱码
        response.setHeader("content-disposition", "application;filename="+ URLEncoder.encode("员工表.xls","UTF-8"));
        ServletOutputStream out = response.getOutputStream();
        workbook.write(out);
        out.close();
    }

    /**
     * 导入员工表
     */
    @ApiOperation(value = "导入员工表")
    @PostMapping(value = "/importEmployee")
    public RespBean importEmployee(MultipartFile file) {
        ImportParams params = new ImportParams();
        //去掉第一行
        params.setTitleRows(1);

        // 获取所有国家列表
        List<Nation> nations = nationService.list();
        List<PoliticsStatus> politicsStatuses = politicsStatusService.list();
        List<Department> departments = departmentService.list();
        List<Joblevel> joblevels = joblevelService.list();
        List<Position> positions = positionService.list();

        try {
            List<Employee> list = ExcelImportUtil.importExcel(file.getInputStream(), Employee.class, params);

            list.forEach(employee -> {
                // 添加国家id
                Integer nationId = nations.get(nations.indexOf(new Nation(employee.getNation().getName()))).getId();
                employee.setNationId(nationId);
                // 政治面貌
                Integer politicId = politicsStatuses.get(politicsStatuses.indexOf(new PoliticsStatus(employee.getPoliticsStatus().getName()))).getId();
                employee.setPoliticId(politicId);
                Integer departmentId = departments.get(departments.indexOf(new Department(employee.getDepartment().getName()))).getId();
                employee.setDepartmentId(departmentId);
                Integer jobId = joblevels.get(joblevels.indexOf(new Joblevel(employee.getJoblevel().getName()))).getId();
                employee.setJobLevelId(jobId);
                Integer pid = positions.get(positions.indexOf(new Position(employee.getPosition().getName()))).getId();
                employee.setPosId(pid);
            });
            if (employeeService.saveBatch(list)){
                return RespBean.sucess("插入成功！");
            }
        }catch (NoSuchElementException e){
            throw new NoSuchElementException("excel文件不能为空");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RespBean.error("插入失败！");
    }
}
