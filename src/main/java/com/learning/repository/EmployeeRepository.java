package com.learning.repository;

import com.learning.entity.Employee;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    Optional<Employee> findByEmployeeName(String employeeName);

    @Modifying(clearAutomatically = true)
    @Query("update Employee e set e.employeeAge = :employeeAge where e.employeeId = :employeeId")
    int updateEmployeeAge(@Param("employeeAge")int employeeAge, @Param("employeeId")Long employeeId);
}
