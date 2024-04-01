package com.management.service;

import com.management.entity.Employee;
import com.management.entity.Timesheet;
import com.management.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TimesheetService {



    @Autowired
    private TimesheetRepository timesheetRepository;

    public List<Timesheet> findTimesheetsByEmployee(Employee employee) {
        return timesheetRepository.findByEmployee(employee);
    }

    public List<Timesheet> findTimesheetsByEmployeeForLast14Days(Employee employee) {
        LocalDateTime twoWeeksAgo = LocalDateTime.now().minusDays(14);
        return timesheetRepository.findByEmployeeAndClockInTimeAfter(employee, twoWeeksAgo);
    }
    public Timesheet findLatestTimesheetByEmployee(Employee employee) {
        List<Timesheet> timesheets = findTimesheetsByEmployee(employee);
        if (!timesheets.isEmpty()) {
            return timesheets.get(timesheets.size() - 1);
        }
        return null;
    }

    public void clockIn(Timesheet timesheet) {
        timesheetRepository.save(timesheet);
    }

    public void clockOut(Timesheet timesheet) {
        timesheetRepository.save(timesheet);
    }

    public Duration calculateTotalWorkHours(Employee employee) {
        List<Timesheet> timesheets = timesheetRepository.findByEmployee(employee);
        Duration totalWorkHours = Duration.ZERO;
        for (Timesheet timesheet : timesheets) {
            if (timesheet.getClockOutTime() != null) {
                totalWorkHours = totalWorkHours.plus(Duration.between(timesheet.getClockInTime(), timesheet.getClockOutTime()));
            }
        }
        return totalWorkHours;
    }

    public Duration calculateTotalWorkHoursForSingleEmployeeAndDay(Employee employee, LocalDateTime date) {
        LocalDateTime startOfDay = date.withHour(0).withMinute(0).withSecond(0);
        LocalDateTime endOfDay = date.withHour(23).withMinute(59).withSecond(59);
        List<Timesheet> timesheets = timesheetRepository.findByEmployeeAndClockInTimeBetween(employee, startOfDay, endOfDay);
        Duration totalWorkHours = Duration.ZERO;
        for (Timesheet timesheet : timesheets) {
            if (timesheet.getClockOutTime() != null) {
                totalWorkHours = totalWorkHours.plus(Duration.between(timesheet.getClockInTime(), timesheet.getClockOutTime()));
            }
        }
        return totalWorkHours;
    }

    public Map<LocalDate, Duration> calculateTotalWorkHoursForAllEmployeesAndDay() {
        Map<LocalDate, Duration> totalWorkHoursMap = new HashMap<>();
        LocalDateTime now = LocalDateTime.now();
        for (int i = 0; i < 7; i++) {
            LocalDateTime date = now.minusDays(i);
            LocalDate localDate = date.toLocalDate();
            List<Timesheet> timesheets = timesheetRepository.findByClockInTimeBetween(date.withHour(0).withMinute(0).withSecond(0),
                    date.withHour(23).withMinute(59).withSecond(59));
            Duration totalWorkHours = Duration.ZERO;
            for (Timesheet timesheet : timesheets) {
                if (timesheet.getClockOutTime() != null) {
                    totalWorkHours = totalWorkHours.plus(Duration.between(timesheet.getClockInTime(), timesheet.getClockOutTime()));
                }
            }
            totalWorkHoursMap.put(localDate, totalWorkHours);
        }
        return totalWorkHoursMap;
    }
    public List<Timesheet> getTimesheetsByEmployeeAndDateRange(Employee employee, LocalDateTime startDate, LocalDateTime endDate) {
        return timesheetRepository.findByEmployeeAndClockInTimeBetween(employee, startDate, endDate);
    }

}

