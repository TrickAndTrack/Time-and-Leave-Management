package com.management.controller;

import com.management.entity.Employee;
import com.management.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

//    GET /employees: Retrieves all employees.
//
//            Method: GET
//    URL: http://localhost:8080/employees
//    Body: No body needed for a GET request.
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }

//    GET /employees/{id}: Retrieves an employee by ID.
//
//            Method: GET
//    URL: http://localhost:8080/employees/{id}
//            (Replace {id} with the actual ID of the employee you want to retrieve.)
//    Body: No body needed for a GET request.
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        Employee employee = employeeService.getEmployeeById(id);
        if (employee != null) {
            return ResponseEntity.ok(employee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    POST /employees: Creates a new employee.
//
//            Method: POST
//    URL: http://localhost:8080/employees
//    Body: JSON object representing the new employee.

//    {
//        "username": "new_employee",
//            "otherField": "otherValue"
//    }

    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.createEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

//    PUT /employees/{id}: Updates an existing employee by ID.
//
//    Method: PUT
//    URL: http://localhost:8080/employees/{id}
//            (Replace {id} with the actual ID of the employee you want to update.)
//    Body: JSON object representing the updated employee.

//    {
//        "username": "updated_username",
//            "otherField": "updatedValue"
//    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
        Employee updatedEmployee = employeeService.updateEmployee(id, employeeDetails);
        if (updatedEmployee != null) {
            return ResponseEntity.ok(updatedEmployee);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    DELETE /employees/{id}: Deletes an employee by ID.
//
//            Method: DELETE
//    URL: http://localhost:8080/employees/{id}
//            (Replace {id} with the actual ID of the employee you want to delete.)
//    Body: No body needed for a DELETE request.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{username}/total-work-hours")
    public ResponseEntity<Map<LocalDate, Duration>> getTotalWorkHoursForSingleEmployeeAndDay(@PathVariable String username) {
        LocalDate today = LocalDate.now();
        Map<LocalDate, Duration> totalWorkHours = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            Duration workHours = employeeService.calculateTotalWorkHoursForSingleEmployeeAndDay(username, date);
            totalWorkHours.put(date, workHours);
        }
        return ResponseEntity.ok(totalWorkHours);
    }

    @GetMapping("/total-work-hours")
    public ResponseEntity<Map<LocalDate, Duration>> getTotalWorkHoursForAllEmployeesAndDay() {
        return ResponseEntity.ok(employeeService.calculateTotalWorkHoursForAllEmployeesAndDay());
    }
}

