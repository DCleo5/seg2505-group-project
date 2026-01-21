package com.utaste.data.model;

import java.time.Instant;
import java.util.Objects;

public abstract class User implements Authenticatable {
    protected long id;
    protected String email;
    protected String firstName;
    protected String lastName;
    protected String passwordHash;
    protected final Instant createdAt;
    protected Instant updatedAt;

    protected User(String email, String passwordHash, String firstName, String lastName) {
        this.email = Objects.requireNonNull(email);
        this.passwordHash = Objects.requireNonNull(passwordHash);
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;
    }

    public abstract Role role();

    public long getId() { return id; }
    public String getEmail() { return email; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }

    public void setProfile(String first, String last, String email) {
        this.firstName = first;
        this.lastName = last;
        this.email = email;
        this.updatedAt = Instant.now();
    }

    @Override public boolean checkPassword(String raw) {
        return Passwords.hash(raw).equals(passwordHash);
    }
    @Override public void setPassword(String newPwd) {
        this.passwordHash = Passwords.hash(newPwd);
        this.updatedAt = Instant.now();
    }
}
