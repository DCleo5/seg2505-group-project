package com.utaste.data.model;
public final class Waiter extends User {
    public Waiter(String email, String pwd, String fn, String ln) {
        super(email, Passwords.hash(pwd), fn, ln);
    }
    @Override public Role role() { return Role.WAITER; }
}
