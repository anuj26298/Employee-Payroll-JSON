package com.json.employeepayroll.service;

import com.json.employeepayroll.entity.EmployeeData;

import java.util.List;

public class EmployeePayrollService {
    List<EmployeeData> employeeDataList;

    public EmployeePayrollService(List<EmployeeData> employeeDataList) {
        this.employeeDataList = employeeDataList;
    }

    public long countEntries() {
        return this.employeeDataList.stream().count();
    }
}
