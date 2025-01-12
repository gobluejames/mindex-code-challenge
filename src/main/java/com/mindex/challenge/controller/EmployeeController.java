package com.mindex.challenge.controller;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

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

    //putting this here since the reportingStructure is tigly coupled to the employee
    @GetMapping("/reportingStructure/{employeeId}")
    public ReportingStructure getReportingStructureForEmployee(@PathVariable String employeeId) {
        //todo validate employeeId and user access (broken access controls)
        return new ReportingStructure(this.employeeService.read(employeeId));
    }
}
