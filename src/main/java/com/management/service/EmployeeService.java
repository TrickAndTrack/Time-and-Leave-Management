package com.management.service;

import com.management.entity.Employee;
import com.management.exceptionHandling.EmployeeNotFoundException;
import com.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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


    public Map<LocalDate, Duration> calculateTotalWorkHoursForSingleEmployeeAndLastSeevanDays(long id) {
        Employee employee = employeeRepository.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found");
        }

        LocalDate today = LocalDate.now();
        Map<LocalDate, Duration> totalWorkHours = new LinkedHashMap<>();
        for (int i = 0; i < 7; i++) {
            LocalDate date = today.minusDays(i);
            Duration workHours = timesheetService.calculateTotalWorkHoursForSingleEmployeeAndDay(employee, date.atStartOfDay());
            totalWorkHours.put(date, workHours);
        }
        return totalWorkHours;
    }



    public Map<LocalDate, Duration> calculateTotalWorkHoursForAllEmployeesAndDay() {
        return timesheetService.calculateTotalWorkHoursForAllEmployeesAndDay();
    }



    public List<Long> getAllEmployeeIds() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream().map(Employee::getId).collect(Collectors.toList());
    }
}