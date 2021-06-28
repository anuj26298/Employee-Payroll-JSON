package com.json.employeepayroll.service;

import com.json.employeepayroll.entity.EmployeeData;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollService {
    List<EmployeeData> employeeDataList;

    public EmployeeData getEmployeeData(String name){
        return this.employeeDataList.stream()
                .filter(employee -> employee.getName().equals(name))
                .findFirst()
                .orElse(null);
    }


    public EmployeePayrollService(List<EmployeeData> employeeDataList) {
        this.employeeDataList = new ArrayList<>(employeeDataList);
    }

    public long countEntries() {
        return this.employeeDataList.stream().count();
    }

    public void addEmployeeToPayroll(EmployeeData employeeData) {
        this.employeeDataList.add(employeeData);
    }

    public void updateSalary(String name, double salary){
        EmployeeData employeeData = this.getEmployeeData(name);
    }

    public void deleteEmployeeData(String name){
        this.employeeDataList.remove(this.getEmployeeData(name));
    }

}
