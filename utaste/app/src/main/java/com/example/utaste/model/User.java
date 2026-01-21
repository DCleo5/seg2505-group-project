package com.example.utaste.model;

import java.util.Date;

public abstract class User {
    protected long id;
    protected String firstName;
    protected String lastName;
    protected String email;
    protected String password;
    protected String role;
    protected Date createdAt;
    protected Date updatedAt;

    public User() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public User(long id, String firstName, String lastName, String email, String password, String role) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public abstract String getRole();

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}