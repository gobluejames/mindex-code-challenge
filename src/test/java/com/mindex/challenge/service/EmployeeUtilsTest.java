package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EmployeeUtilsTest {

    @Test
    public void testCalculateNumberOfReports_NoDirectReports_ReturnsNoDirectReports() {
        Employee emp = this.createEmployee("E1", Collections.emptyList());
        int result = EmployeeUtils.calculateNumberOfReports(emp);
        assertEquals(0, result);
    }

    @Test
    public void testCalculateNumberOfReports_WithDirectReports_ReturnsTheCorrectNumberOfReports() {
        Employee emp2 = this.createEmployee("E2", Collections.emptyList());
        Employee emp3 = this.createEmployee("E3", Collections.emptyList());
        Employee emp1 = this.createEmployee("E1", Arrays.asList(emp2, emp3));

        int result = EmployeeUtils.calculateNumberOfReports(emp1);
        assertEquals(2, result);
    }

    @Test
    public void testCalculateNumberOfReports_WithNestedReports_ReturnsTheCorrectNumberOfReports() {
        Employee emp4 = this.createEmployee("E4", Collections.emptyList());
        Employee emp5 = this.createEmployee("E5", Collections.emptyList());
        Employee emp2 = this.createEmployee("E2", Arrays.asList(emp4, emp5));
        Employee emp3 = this.createEmployee("E3", Collections.emptyList());
        Employee emp1 = this.createEmployee("E1", Arrays.asList(emp2, emp3));

        int result = EmployeeUtils.calculateNumberOfReports(emp1);
        assertEquals(4, result);
    }

    @Test
    public void testCalculateNumberOfReports_NullDirectReports_ReturnsNoReports() {
        Employee emp = this.createEmployee("E1", null);
        int result = EmployeeUtils.calculateNumberOfReports(emp);
        assertEquals(0, result);
    }

    @Test
    public void testCalculateNumberOfReports_NullEmployee_ReturnsNoReports() {
        int result = EmployeeUtils.calculateNumberOfReports(null);
        assertEquals(0, result);
    }

    /**
     * Helper function to allow concise employee creation in tests
     */
    private Employee createEmployee(String employeeId, List<Employee> directReports) {
        final Employee employee = new Employee();
        employee.setEmployeeId(employeeId);
        employee.setDirectReports(directReports);
        return employee;
    }
}
