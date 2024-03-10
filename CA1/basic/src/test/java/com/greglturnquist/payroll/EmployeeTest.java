package com.greglturnquist.payroll;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeTest {

    @Test
    void testEmployeeWithValidParameters() {
        Employee employee = new Employee("Frodo", "Baggins", "ring bearer", 1, "Janitor");
        assertEquals("Frodo", employee.getFirstName());
        assertEquals("Baggins", employee.getLastName());
        assertEquals("ring bearer", employee.getDescription());
        assertEquals(1, employee.getJobYears());
        assertEquals("Janitor", employee.getJobTitle());
    }

    @Test
    void testEmployeeWithNullParameters() {
        assertThrows(IllegalArgumentException.class, () -> new Employee(null, "Baggins", "ring bearer", 1, "Janitor"));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Frodo", null, "ring bearer", 1, "Janitor"));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Frodo", "Baggins", null, 1, "Janitor"));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Frodo", "Baggins", "ring bearer", 1, null));
    }

    @Test
    void testEmployeeWithBlankParameters() {
        assertThrows(IllegalArgumentException.class, () -> new Employee("", "Baggins", "ring bearer", 1, "Janitor"));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Frodo", "", "ring bearer", 1, "Janitor"));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Frodo", "Baggins", "", 1, "Janitor"));
        assertThrows(IllegalArgumentException.class, () -> new Employee("Frodo", "Baggins", "ring bearer", 1, ""));
    }

    @Test
    void testEmployeeWithNegativeJobYears() {
        assertThrows(IllegalArgumentException.class, () -> new Employee("Frodo", "Baggins", "ring bearer", -1, "Janitor"));
    }
}