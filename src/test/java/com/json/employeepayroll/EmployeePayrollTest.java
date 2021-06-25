package com.json.employeepayroll;

import com.google.gson.Gson;
import com.json.employeepayroll.entity.EmployeeData;
import com.json.employeepayroll.service.EmployeePayrollService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class EmployeePayrollTest {

    @Before
    public void setUp() throws Exception {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public EmployeeData[] getEmployeeList(){
        Response response = RestAssured.get("/employees");
        System.out.println("Employee payroll Entries in JSON:\n" + response.asString());
        EmployeeData[] employeeDataArray = new Gson().fromJson(response.asString(), EmployeeData[].class);
        return  employeeDataArray;
    }


    @Test
    public void givenEmployeeDataInJSONServer_WhenRetrieved_ReturnCountOfEntries() {
        EmployeeData[] employeeData = getEmployeeList();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(employeeData));
        long entries = employeePayrollService.countEntries();
        Assert.assertEquals(3,entries);
    }
}
