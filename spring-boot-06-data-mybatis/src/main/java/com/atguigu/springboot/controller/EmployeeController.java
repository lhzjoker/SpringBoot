package com.atguigu.springboot.controller;

import com.atguigu.springboot.bean.Employee;
import com.atguigu.springboot.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

    @Autowired
    EmployeeMapper employeeMapper;

    @GetMapping("/emp/{id}")
    public Employee getEmpById(@PathVariable("id") Integer id){

        return employeeMapper.getEmpById(id);
    }

    @GetMapping("/emp")
    public Employee insertEmp(Employee employee){
        employeeMapper.insertEmp(employee);
        return employee;
    }
}
