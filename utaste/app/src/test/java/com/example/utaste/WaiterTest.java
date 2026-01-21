package com.example.utaste;

import com.example.utaste.model.Waiter;
import org.junit.Test;

import static org.junit.Assert.*;

public class WaiterTest {

    @Test
    public void defaultConstructor_setsRoleToWaiter() {
        Waiter waiter = new Waiter();

        assertEquals("WAITER", waiter.getRole());
        assertEquals(0, waiter.getId());
        assertNull(waiter.getFirstName());
        assertNull(waiter.getLastName());
        assertNull(waiter.getEmail());
    }

    @Test
    public void parameterizedConstructor_setsAllFieldsCorrectly() {
        Waiter waiter = new Waiter(
                1L,
                "Alain",
                "Niyonkuru",
                "alain@example.com",
                "password123"
        );

        assertEquals(1L, waiter.getId());
        assertEquals("Alain", waiter.getFirstName());
        assertEquals("Niyonkuru", waiter.getLastName());
        assertEquals("alain@example.com", waiter.getEmail());

        assertEquals("WAITER", waiter.getRole());
    }

    @Test
    public void setters_updateProperties() {
        Waiter waiter = new Waiter();

        waiter.setId(10L);
        waiter.setFirstName("Marie");
        waiter.setLastName("Dupont");
        waiter.setEmail("marie@example.com");
        waiter.setPassword("secret");

        assertEquals(10L, waiter.getId());
        assertEquals("Marie", waiter.getFirstName());
        assertEquals("Dupont", waiter.getLastName());
        assertEquals("marie@example.com", waiter.getEmail());

        assertEquals("WAITER", waiter.getRole());
    }
}
