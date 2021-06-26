package com.json.employeepayroll;

import com.google.gson.Gson;
import com.json.employeepayroll.entity.EmployeeData;
import com.json.employeepayroll.service.EmployeePayrollService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class EmployeePayrollTest {

    @Before
    public void setUp(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3000;
    }

    public EmployeeData[] getEmployeeList(){
        Response response = RestAssured.get("/employees");
        System.out.println("Employee payroll Entries in JSON:\n" + response.asString());
        return new Gson().fromJson(response.asString(), EmployeeData[].class);
    }

    public Response addEmployeeDataToJsonServer(EmployeeData employeeData){
        String empJson = new Gson().toJson(employeeData);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body(empJson);
        return requestSpecification.post("/employees");
    }


    @Test
    public void givenEmployeeDataInJSONServer_WhenRetrieved_ReturnCountOfEntries() {
        EmployeeData[] employeeData = getEmployeeList();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(employeeData));
        long entries = employeePayrollService.countEntries();
        Assert.assertEquals(4,entries);
    }

    @Test
    public void givenNewEmployeeData_WhenAddedToJsonServerFile_Return201ResponseAndCountOfEntries() {

        EmployeeData employeeData = new EmployeeData(4,"Bob",30000);
        Response response = addEmployeeDataToJsonServer(employeeData);
        int statusCode = response.getStatusCode();
        Assert.assertEquals(500,statusCode);

        EmployeeData[] employeeData1 = getEmployeeList();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(employeeData1));
        employeePayrollService.addEmployeeToPayroll(employeeData);
        long entries = employeePayrollService.countEntries();
        Assert.assertEquals(4,entries);
    }
}
