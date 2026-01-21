package com.example.utaste.model;

import org.junit.Test;
import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void testUserCreation() {
        // Using Chef as concrete implementation of User
        Chef user = new Chef(1, "John", "Doe", "john@example.com", "password123");

        assertEquals(1, user.getId());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("CHEF", user.getRole());
    }

    @Test
    public void testSetters() {
        Chef user = new Chef();
        user.setFirstName("Jane");
        user.setLastName("Smith");
        user.setEmail("jane@example.com");
        user.setPassword("newPass");

        assertEquals("Jane", user.getFirstName());
        assertEquals("Smith", user.getLastName());
        assertEquals("jane@example.com", user.getEmail());
        assertEquals("newPass", user.getPassword());
    }

    // Robustness Tests

    @Test
    public void testNullFirstName() {
        // Robustness: Check if it accepts null (POJO behavior)
        Chef user = new Chef(1, null, "Doe", "email", "pass");
        assertNull(user.getFirstName());
    }

    @Test
    public void testInvalidEmailFormat() {
        // Robustness: Check if it accepts invalid email (POJO behavior)
        Chef user = new Chef(1, "John", "Doe", "invalid-email", "pass");
        assertEquals("invalid-email", user.getEmail());
    }
}
