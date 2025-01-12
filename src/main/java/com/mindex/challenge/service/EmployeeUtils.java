package com.mindex.challenge.service;

import com.mindex.challenge.data.Employee;

/**
 * Utility functions that apply to the Employee class. I thought this made more sense
 * here instead of on the Employee object, since it will be called w/ an Employee param.
 */
public class EmployeeUtils {

    /**
     * Recursive method used to calculate the number of direct reports.
     */
    public static Integer calculateNumberOfReports(Employee employee) {
        if (employee == null ||
                employee.getDirectReports() == null ||
                employee.getDirectReports().isEmpty()) {
            return 0;
        }

        int count = 0;
        for (Employee directReport : employee.getDirectReports()) {
            count += 1 + calculateNumberOfReports(directReport); // 1 for the direct report itself and the nested reports
        }

        return count;
    }

}
