package com.utaste.data.model;
public final class Administrator extends User {
    public Administrator(String email, String pwd, String fn, String ln) {
        super(email, Passwords.hash(pwd), fn, ln);
    }
    @Override public Role role() { return Role.ADMINISTRATOR; }
}
