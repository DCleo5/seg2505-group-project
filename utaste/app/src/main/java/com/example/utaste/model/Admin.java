package com.example.utaste.model;

public class Admin extends User {
    public Admin() {
        super();
        this.role = "ADMINISTRATOR";
    }

    public Admin(long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password, "ADMINISTRATOR");
    }

    @Override
    public String getRole() {
        return "ADMINISTRATOR";
    }
}