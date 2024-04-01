package com.management.service;

import com.management.entity.Employee;
import com.management.exceptionHandling.EmployeeNotFoundException;
import com.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {
    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee findByUsername(String username) {
        return employeeRepository.findByUsername(username);
    }

    public Employee findById(long id) {
        return employeeRepository.findById(id);
    }

    // Retrieve all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Retrieve an employee by ID
    public Employee getEmployeeById(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        return optionalEmployee.orElse(null);
    }

    // Create a new employee
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // Update an existing employee
    public Employee updateEmployee(Long id, Employee employeeDetails) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()) {
            Employee existingEmployee = optionalEmployee.get();
            existingEmployee.setUsername(employeeDetails.getUsername());
            // Update other fields as needed
            return employeeRepository.save(existingEmployee);
        } else {
            return null;
        }
    }

    // Delete an employee
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public Duration calculateTotalWorkHoursForSingleEmployeeAndDay(String username, LocalDate date) {
        Employee employee = employeeRepository.findByUsername(username);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found");
        }
        return timesheetService.calculateTotalWorkHoursForSingleEmployeeAndDay(employee, date.atStartOfDay());
    }

    public Map<LocalDate, Duration> calculateTotalWorkHoursForAllEmployeesAndDay() {
        return timesheetService.calculateTotalWorkHoursForAllEmployeesAndDay();
    }
}