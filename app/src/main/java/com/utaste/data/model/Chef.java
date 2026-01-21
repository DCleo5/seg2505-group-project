package com.utaste.data.model;
public final class Chef extends User {
    public Chef(String email, String pwd, String fn, String ln) {
        super(email, Passwords.hash(pwd), fn, ln);
    }
    @Override public Role role() { return Role.CHEF; }
}
