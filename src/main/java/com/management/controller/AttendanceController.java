package com.management.controller;

import com.management.entity.Attendance;
import com.management.entity.Employee;
import com.management.entity.Timesheet;
import com.management.service.AttendanceService;
import com.management.service.EmployeeService;
import com.management.service.TimesheetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/attendance")
    public List<Timesheet> generateAttendanceReport(@RequestParam("employeeId") Long employeeId) {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7); // 7 days ago
        LocalDateTime endDate = LocalDateTime.now();

        Employee employee = employeeService.getEmployeeById(employeeId);
        if (employee == null) {
            // Handle employee not found scenario
            return null;
        }

        return timesheetService.getTimesheetsByEmployeeAndDateRange(employee, startDate, endDate);
    }

    @Autowired
    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/attendance/daily")
    public List<Attendance> getAttendanceByDaily(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.getAttendanceByDaily(date);
    }

    @GetMapping("/attendance/weekly")
    public List<Attendance> getAttendanceByWeekly(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                  @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return attendanceService.getAttendanceByWeekly(startDate, endDate);
    }

    @GetMapping("/attendance/monthly")
    public List<Attendance> getAttendanceByMonthly(@RequestParam int year, @RequestParam int month) {
        return attendanceService.getAttendanceByMonthly(year, month);
    }


}

