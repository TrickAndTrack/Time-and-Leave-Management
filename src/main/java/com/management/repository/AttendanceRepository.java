package com.management.repository;

import com.management.entity.Attendance;
import com.management.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Attendance findByEmployeeIdAndDate(Long employeeId, LocalDate date);

    Attendance findByEmployeeAndDate(Employee employee, LocalDate date);
}

