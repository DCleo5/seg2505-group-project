package com.example.utaste.model;

public class Chef extends User {
    public Chef() {
        super();
        this.role = "CHEF";
    }

    public Chef(long id, String firstName, String lastName, String email, String password) {
        super(id, firstName, lastName, email, password, "CHEF");
    }

    @Override
    public String getRole() {
        return "CHEF";
    }
}