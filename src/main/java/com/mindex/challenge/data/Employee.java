package com.mindex.challenge.data;

import org.springframework.data.annotation.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Employee {
    // Without this, a second mongo entry will be created for the same client upon the
    // 'update' function being called.
    @Id
    private String employeeId;
    private String firstName;
    private String lastName;
    private String position;
    private String department;
    private List<Employee> directReports;
    private List<Compensation> compensation;

    public Employee() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public List<Employee> getDirectReports() {
        return directReports;
    }

    public void setDirectReports(List<Employee> directReports) {
        this.directReports = directReports;
    }

    public List<Compensation> getCompensation() {
        return compensation;
    }

    public void setCompensation(List<Compensation> compensation) {
        this.compensation = compensation;
    }

    public void addNewCompensation(Compensation newCompensation) {
        if (this.compensation == null) {
            this.compensation = new ArrayList<>();
        }
        this.compensation.add(newCompensation);
    }

    public Compensation getCurrentCompensation() {
        LocalDate today = LocalDate.now();
        return this.compensation.stream()
                .filter(compensation -> compensation.getEffectiveDate() != null && !compensation.getEffectiveDate().isAfter(today))
                .max(Comparator.comparing(Compensation::getEffectiveDate))
                .orElse(null);
    }
}
