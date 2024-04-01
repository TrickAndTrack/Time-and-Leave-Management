package com.management.repository;

import com.management.entity.Employee;
import com.management.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    List<Timesheet> findByEmployee(Employee employee);
    List<Timesheet> findByEmployeeAndClockInTimeAfter(Employee employee, LocalDateTime time);

    List<Timesheet> findByEmployeeAndClockInTimeBetween(Employee employee, LocalDateTime startDate, LocalDateTime endDate);
    List<Timesheet> findByClockInTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

}