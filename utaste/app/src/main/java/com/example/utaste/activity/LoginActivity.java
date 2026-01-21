package com.example.utaste.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.database.UserDao;
import com.example.utaste.model.User;
import com.example.utaste.util.SessionManager;

public class LoginActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private Button loginButton;
    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = new UserDao(this);
        sessionManager = SessionManager.getInstance(this);

        if (sessionManager.isLoggedIn()) {
            redirectToRoleActivity();
            return;
        }

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
        loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(v -> performLogin());
    }

    private void performLogin() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        User user = userDao.login(email, password);
        if (user != null) {
            sessionManager.setCurrentUser(user);
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
            redirectToRoleActivity();
        } else {
            Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private void redirectToRoleActivity() {
        User user = sessionManager.getCurrentUser();
        Intent intent;

        switch (user.getRole()) {
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
                Toast.makeText(this, "Invalid role", Toast.LENGTH_SHORT).show();
                return;
        }

        startActivity(intent);
        finish();
    }
}