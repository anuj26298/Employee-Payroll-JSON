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
        Assert.assertEquals(201,statusCode);

        EmployeeData[] employeeData1 = getEmployeeList();
        EmployeePayrollService employeePayrollService = new EmployeePayrollService(Arrays.asList(employeeData1));
        employeePayrollService.addEmployeeToPayroll(employeeData);
        long entries = employeePayrollService.countEntries();
        Assert.assertEquals(4,entries);
    }

    @Test
    public void givenMultipleEmployeesData_WhenAddedToJsonServerFile_Return201ResponseAndCountOfEntries() {
        EmployeePayrollService employeePayrollService;
        EmployeeData[] employeeData = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(employeeData));
        EmployeeData[] employeeData1 = {
                new EmployeeData(6,"Alien", 3567000),
                new EmployeeData(7,"Alan", 405000)
        };
        Arrays.stream(employeeData1).forEach(employee ->{
            Response response = addEmployeeDataToJsonServer(employee);
            int statusCode = response.getStatusCode();
            Assert.assertEquals(500, statusCode);

            employee = new Gson().fromJson(response.asString(), EmployeeData.class);
            employeePayrollService.addEmployeeToPayroll(employee);
        });
        long entries = employeePayrollService.countEntries();
        Assert.assertEquals(7,entries);
    }

    @Test
    public void givenEmployeeNewSalary_WhenUpdated_Return200Response() {
        EmployeePayrollService employeePayrollService;
        EmployeeData[] employeeData = getEmployeeList();
        employeePayrollService = new EmployeePayrollService(Arrays.asList(employeeData));
        employeePayrollService.updateSalary("Alan", 400000);
        EmployeeData employeeData1 = employeePayrollService.getEmployeeData("Alan");

        String empJson = new Gson().toJson(employeeData1);
        RequestSpecification requestSpecification = RestAssured.given();
        requestSpecification.header("Content-Type","application/json");
        requestSpecification.body(empJson);
        Response response = requestSpecification.put("/employees/" + employeeData1.getId());
        Assert.assertEquals(200, response.statusCode());

    }
}
