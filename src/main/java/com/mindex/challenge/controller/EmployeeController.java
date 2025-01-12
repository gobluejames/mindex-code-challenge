package com.mindex.challenge.controller;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
// apply "/employee" to all endpoints in the controller
@RequestMapping("/employee")
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    private final EmployeeService employeeService;

    // Injected dependencies can be marked final to make them immutable when using
    // constructor injection; this also makes testing easier because we don't need to
    // rely on the Spring context
    EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @PostMapping("")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee create request for id [{}]", id);

        return employeeService.read(id);
    }

    @PutMapping("/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee create request for id [{}] and employee [{}]", id, employee);

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    //putting this here since the reportingStructure is tightly coupled to the employee
    @GetMapping("/reportingStructure/{employeeId}")
    public ReportingStructure getReportingStructureForEmployee(@PathVariable String employeeId) {
        //todo validate employeeId and user access (broken access controls)
        return new ReportingStructure(this.employeeService.read(employeeId));
    }

    @PutMapping("/compensation/{employeeId}")
    // void because we don't want to return info that wasn't passed in (security)
    public void addNewCompensation(@PathVariable String employeeId, @Valid @RequestBody Compensation compensation) {
        //todo validate employeeId and user access (broken access controls)
        this.employeeService.addNewCompensation(employeeId, compensation);
    }

    @GetMapping("/compensation/{employeeId}")
    public List<Compensation> getCompensationHistoryForEmployee(@PathVariable String employeeId) {
        //todo validate employeeId and user access (broken access controls)
        return this.employeeService.read(employeeId).getCompensation();
    }
}
