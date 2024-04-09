package com.management.task;

import com.management.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class AttendanceUpdateTask {

    @Autowired
    private AttendanceService attendanceService;

    @Scheduled(cron = "0 0 2 * * *") // Runs at 2:00 AM every day and Runs at midnight every day
    public void updateAttendance() {
        LocalDate currentDate = LocalDate.now().minusDays(1); // Update attendance for the previous day
        attendanceService.updateAttendanceForAllEmployees(currentDate);
    }
}

