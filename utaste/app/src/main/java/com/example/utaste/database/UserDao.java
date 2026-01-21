package com.example.utaste.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.utaste.model.Admin;
import com.example.utaste.model.Chef;
import com.example.utaste.model.User;
import com.example.utaste.model.Waiter;

import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private DatabaseHelper dbHelper;

    public UserDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public User login(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "email = ? AND password = ?",
                new String[] { email, password }, null, null, null);

        if (cursor.moveToFirst()) {
            User user = createUserFromCursor(cursor);
            cursor.close();
            return user;
        }
        cursor.close();
        return null;
    }

    public long createUser(String firstName, String lastName, String email, String password, String role) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("password", password);
        values.put("role", role);
        values.put("created_at", System.currentTimeMillis());
        values.put("updated_at", System.currentTimeMillis());
        return db.insert("users", null, values);
    }

    public long createWaiter(String firstName, String lastName, String email, String password) {
        return createUser(firstName, lastName, email, password, "WAITER");
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, null,
                null, null, null, "role ASC, email ASC");

        while (cursor.moveToNext()) {
            User user = createUserFromCursor(cursor);
            if (user != null) {
                users.add(user);
            }
        }
        cursor.close();
        return users;
    }

    public List<Waiter> getAllWaiters() {
        List<Waiter> waiters = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("users", null, "role = ?",
                new String[] { "WAITER" }, null, null, "email ASC");

        while (cursor.moveToNext()) {
            waiters.add((Waiter) createUserFromCursor(cursor));
        }
        cursor.close();
        return waiters;
    }

    public boolean deleteUser(long userId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("users", "id = ?",
                new String[] { String.valueOf(userId) }) > 0;
    }

    public boolean resetPasswordByEmail(String email, String defaultPassword) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", defaultPassword);
        values.put("updated_at", System.currentTimeMillis());

        int rows = db.update("users", values, "email = ?", new String[] { email });
        return rows > 0;
    }

    public boolean updateUser(long userId, String firstName, String lastName, String email) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("first_name", firstName);
        values.put("last_name", lastName);
        values.put("email", email);
        values.put("updated_at", System.currentTimeMillis());
        return db.update("users", values, "id = ?",
                new String[] { String.valueOf(userId) }) > 0;
    }

    public void resetDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("sales", null, null);
        db.delete("recipe_ingredients", null, null);
        db.delete("ingredients", null, null);
        db.delete("recipes", null, null);
        db.delete("users", "role = ?", new String[] { "WAITER" });
    }

    private User createUserFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        String firstName = cursor.getString(cursor.getColumnIndexOrThrow("first_name"));
        String lastName = cursor.getString(cursor.getColumnIndexOrThrow("last_name"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
        String password = cursor.getString(cursor.getColumnIndexOrThrow("password"));
        String role = cursor.getString(cursor.getColumnIndexOrThrow("role"));

        switch (role) {
            case "ADMINISTRATOR":
                return new Admin(id, firstName, lastName, email, password);
            case "CHEF":
                return new Chef(id, firstName, lastName, email, password);
            case "WAITER":
                return new Waiter(id, firstName, lastName, email, password);
            default:
                return null;
        }
    }
}