package com.management.service;

import com.management.entity.Attendance;
import com.management.entity.Employee;
import com.management.repository.AttendanceRepository;
import com.management.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AttendanceService {

    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private EmployeeService  employeeService;
    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    AttendanceRepository repository;


    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    public List<Attendance> getAttendanceByWeekly(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }

    public List<Attendance> getAttendanceByMonthly(int year, int month) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }

    public List<Attendance> getAttendanceByDaily(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }


}

