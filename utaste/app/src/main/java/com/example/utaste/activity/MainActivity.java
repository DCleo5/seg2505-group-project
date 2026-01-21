package com.example.utaste.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.util.SessionManager;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = SessionManager.getInstance(this);

        new Handler().postDelayed(() -> {
            if (sessionManager.isLoggedIn()) {
                redirectToRoleActivity();
            } else {
                startActivity(new Intent(this, LoginActivity.class));
            }
            finish();
        }, 500);
    }

    private void redirectToRoleActivity() {
        String role = sessionManager.getCurrentUser().getRole();
        Intent intent;

        switch (role) {
            case "ADMINISTRATOR":
                intent = new Intent(this, AdminActivity.class);
                break;
            case "CHEF":
                intent = new Intent(this, ChefRecipesActivity.class);
                break;
            case "WAITER":
                intent = new Intent(this, WaiterActivity.class);
                break;
            default:
                sessionManager.clearSession();
                intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
    }
}