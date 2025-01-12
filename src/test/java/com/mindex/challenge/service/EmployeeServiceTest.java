package com.mindex.challenge.service;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeServiceTest {

    private String employeeUrl;
    private String employeeIdUrl;
    private String employeeCompensationUrl;
    Employee testEmployee;
    Employee createdEmployee;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        employeeUrl = "http://localhost:" + port + "/employee";
        employeeIdUrl = "http://localhost:" + port + "/employee/{id}";
        employeeCompensationUrl = "http://localhost:" + port + "/employee/compensation/";

        testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        testEmployee.addNewCompensation(new Compensation(BigDecimal.valueOf(50000), LocalDate.now().minusYears(4)));
        testEmployee.addNewCompensation(new Compensation(BigDecimal.valueOf(55000), LocalDate.now().minusYears(2)));

        // Create checks
        createdEmployee = restTemplate.postForEntity(employeeUrl, testEmployee, Employee.class).getBody();
    }

    @Test
    public void testCreateReadUpdate() {
        assertNotNull(createdEmployee.getEmployeeId());
        assertEmployeeEquivalence(testEmployee, createdEmployee);
        assertEmployeeCompensationEquivalence(testEmployee.getCompensation(), createdEmployee.getCompensation());


        // Read checks
        Employee readEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();
        assertEquals(createdEmployee.getEmployeeId(), readEmployee.getEmployeeId());
        assertEquals(createdEmployee.getCompensation().size(), readEmployee.getCompensation().size());
        assertEmployeeEquivalence(createdEmployee, readEmployee);
        assertEmployeeCompensationEquivalence(createdEmployee.getCompensation(), readEmployee.getCompensation());

        // Update checks
        readEmployee.setPosition("Development Manager");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Employee updatedEmployee =
                restTemplate.exchange(employeeIdUrl,
                        HttpMethod.PUT,
                        new HttpEntity<Employee>(readEmployee, headers),
                        Employee.class,
                        readEmployee.getEmployeeId()).getBody();

        assertEmployeeEquivalence(readEmployee, updatedEmployee);
        assertEmployeeCompensationEquivalence(
                readEmployee.getCompensation(),
                updatedEmployee.getCompensation()
        );
    }


    @Test
    public void testAddNewCompensation() {
        //Update Compensation check
        restTemplate.put(employeeCompensationUrl + "/" + createdEmployee.getEmployeeId(), new Compensation(BigDecimal.valueOf(65000), LocalDate.now()));

        // Read
        Employee updatedEmployee = restTemplate.getForEntity(employeeIdUrl, Employee.class, createdEmployee.getEmployeeId()).getBody();

        assertEquals(createdEmployee.getCompensation().size() + 1 , updatedEmployee.getCompensation().size());
        assertEquals(createdEmployee.getCurrentCompensation().getSalary(), BigDecimal.valueOf(55000));
        assertEquals(updatedEmployee.getCurrentCompensation().getSalary(), BigDecimal.valueOf(65000));
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
        assertEquals(expected.getCurrentCompensation().getEffectiveDate(), actual.getCurrentCompensation().getEffectiveDate());
        assertEquals(expected.getCurrentCompensation().getSalary(), actual.getCurrentCompensation().getSalary());
    }

    private static void assertEmployeeCompensationEquivalence(List<Compensation> expected, List<Compensation> actual) {
        assertEquals(expected.size(), actual.size());
        assertArrayEquals(expected.toArray(), actual.toArray());
    }
}
