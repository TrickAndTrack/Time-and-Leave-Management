package com.management.controller;

import com.management.entity.Employee;
import com.management.entity.Timesheet;
import com.management.service.EmployeeService;
import com.management.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/timesheet")
public class TimesheetController {

    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/clockin")
    public ResponseEntity<String> clockIn(@RequestParam String username) {
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.badRequest().body("Employee not found.");
        }

        Timesheet existingTimesheet = timesheetService.findLatestTimesheetByEmployee(employee);
        if (existingTimesheet != null && existingTimesheet.getClockOutTime() == null) {
            return ResponseEntity.badRequest().body("You have already clocked in.");
        }

        Timesheet timesheet = new Timesheet();
        timesheet.setEmployee(employee);
        timesheet.setClockInTime(LocalDateTime.now());
        timesheetService.clockIn(timesheet);
        return ResponseEntity.ok("Clock-in successful.");
    }

    @PostMapping("/clock-out")
    public ResponseEntity<String> clockOut(@RequestParam String username) {
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.badRequest().body("Employee not found.");
        }

        Timesheet existingTimesheet = timesheetService.findLatestTimesheetByEmployee(employee);
        if (existingTimesheet == null || existingTimesheet.getClockOutTime() != null) {
            return ResponseEntity.badRequest().body("You have already clocked out or not clocked in yet.");
        }

        existingTimesheet.setClockOutTime(LocalDateTime.now());
        timesheetService.clockOut(existingTimesheet);
        return ResponseEntity.ok("Clock-out successful.");
    }
    @GetMapping("/{username}/total-work-hours")
    public ResponseEntity<String> getTotalWorkHours(@PathVariable String username) {
        Employee employee = employeeService.findByUsername(username);
        if (employee == null) {
            return ResponseEntity.badRequest().body("Employee not found.");
        }

        Duration totalWorkHours = timesheetService.calculateTotalWorkHours(employee);
        return ResponseEntity.ok("Total work hours: " + totalWorkHours.toHours() + " hours");
    }


    }
