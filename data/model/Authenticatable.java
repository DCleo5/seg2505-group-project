package com.utaste.data.model;
public interface Authenticatable {
    boolean checkPassword(String raw);
    void setPassword(String newPwd);
}
