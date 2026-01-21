package com.example.utaste.model;

public class Waiter extends User {
    public Waiter() {
        super();
        this.role = "WAITER";
    }

    public Waiter(long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password, "WAITER");
    }

    @Override
    public String getRole() {
        return "WAITER";
    }
}