package com.learning.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.entity.Employee;
import com.learning.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/jpa")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    private ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private EmployeeRepository repository;

    @RequestMapping(value = "/marco")
    public ResponseEntity<Object> polo() {
        return new ResponseEntity<>("polo", HttpStatus.OK);
    }

    @RequestMapping(value = "/create")
    public ResponseEntity<Object> createEmployee(Employee employee) {
        logger.info("Creating new employee:::");
        repository.save(employee);
        return new ResponseEntity<>("Employee created", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/getall")
    public ResponseEntity<Object> getAllEmployees() throws JsonProcessingException {
        logger.info("Fetching all employees");
        Iterable<Employee> employeeItr = repository.findAll();
        final List<Employee> employeeList = new ArrayList<>();
        employeeItr.forEach(employeeList::add);
        return new ResponseEntity<>(mapper.writeValueAsString(employeeList), HttpStatus.OK);
    }

    @RequestMapping(value = "/getbyid")
    public ResponseEntity<Object> getEmployeeById(@RequestParam(name = "employeeId")Long employeeId) throws JsonProcessingException {
        logger.info("Fetching employee with id: {}", employeeId);
        Optional<Employee> employee = repository.findById(employeeId);
        if (employee.isPresent()) {
            return new ResponseEntity<>(mapper.writeValueAsString(employee.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getbyname")
    public ResponseEntity<Object> getEmployeeByName(@RequestParam(name = "employeeName")String employeeName) throws JsonProcessingException {
        logger.info("Fetching emplyee with name: {}", employeeName);
        Optional<Employee> employee = repository.findByEmployeeName(employeeName);
        if(employee.isPresent()) {
            return new ResponseEntity<>(mapper.writeValueAsString(employee.get()), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Employee not found", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/updateage")
    @Transactional
    public ResponseEntity<Object> updateEmployeeAge(@RequestParam(name = "employeeId")Long employeeId, @RequestParam(name = "employeeAge")int employeeAge) {
        logger.info("Updating age to {} for employee with id {}", employeeAge, employeeId);
        final int result = repository.updateEmployeeAge(employeeAge, employeeId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/deletebyid")
    @Transactional
    public ResponseEntity<Object>  deleteEmployee(@RequestParam(name = "employeeId")Long employeeId) {
        logger.info("Deleting employee with id: {}", employeeId);
        repository.deleteById(employeeId);
        return new ResponseEntity<>("Employee deleted", HttpStatus.OK);
    }

}
