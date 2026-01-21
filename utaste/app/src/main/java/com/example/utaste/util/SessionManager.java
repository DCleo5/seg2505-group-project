package com.example.utaste.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.utaste.model.Admin;
import com.example.utaste.model.Chef;
import com.example.utaste.model.User;
import com.example.utaste.model.Waiter;
import com.google.gson.Gson;

public class SessionManager {
    private static final String PREF_NAME = "uTasteSession";
    private static final String KEY_USER = "current_user";
    private static final String KEY_ROLE = "user_role";
    private static SessionManager instance;
    private final SharedPreferences prefs;
    private final Gson gson;

    private SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context.getApplicationContext());
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        prefs.edit()
                .putString(KEY_USER, gson.toJson(user))
                .putString(KEY_ROLE, user.getRole())
                .apply();
    }

    public User getCurrentUser() {
        String userJson = prefs.getString(KEY_USER, null);
        String role = prefs.getString(KEY_ROLE, null);

        if (userJson == null || role == null) return null;

        // Deserialize to correct subclass based on role
        switch (role) {
            case "ADMINISTRATOR":
                return gson.fromJson(userJson, Admin.class);
            case "CHEF":
                return gson.fromJson(userJson, Chef.class);
            case "WAITER":
                return gson.fromJson(userJson, Waiter.class);
            default:
                return null;
        }
    }

    public void clearSession() {
        prefs.edit()
                .remove(KEY_USER)
                .remove(KEY_ROLE)
                .apply();
    }

    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }
}