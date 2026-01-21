package com.example.utaste.database;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.utaste.model.Waiter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class UserDaoIntegrationTest {

    private UserDao userDao;
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        userDao = new UserDao(context);
        userDao.resetDatabase(); // Ensure clean state
    }

    @After
    public void tearDown() {
        userDao.resetDatabase();
    }

    @Test
    public void testCreateAndGetWaiter() {
        long id = userDao.createWaiter("John", "Doe", "john@example.com", "pass");
        assertTrue(id > 0);

        List<Waiter> waiters = userDao.getAllWaiters();
        assertFalse(waiters.isEmpty());

        boolean found = false;
        for (Waiter w : waiters) {
            if (w.getEmail().equals("john@example.com")) {
                assertEquals("John", w.getFirstName());
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    public void testUpdateUser() {
        long id = userDao.createWaiter("Jane", "Doe", "jane@example.com", "pass");

        // We need the ID to update. The createWaiter returns the row ID, which might be
        // the user ID if autoincrement matches.
        // Let's assume the returned ID is the user ID.

        boolean updated = userDao.updateUser(id, "JaneUpdated", "DoeUpdated", "jane@example.com");
        assertTrue(updated);

        List<Waiter> waiters = userDao.getAllWaiters();
        for (Waiter w : waiters) {
            if (w.getId() == id) {
                assertEquals("JaneUpdated", w.getFirstName());
                assertEquals("DoeUpdated", w.getLastName());
            }
        }
    }

    @Test
    public void testDeleteUser() {
        long id = userDao.createWaiter("Delete", "Me", "delete@example.com", "pass");
        boolean deleted = userDao.deleteUser(id);
        assertTrue(deleted);

        List<Waiter> waiters = userDao.getAllWaiters();
        for (Waiter w : waiters) {
            assertNotEquals(id, w.getId());
        }
    }
}
