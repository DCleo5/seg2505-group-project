package com.example.utaste.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.database.UserDao;
import com.example.utaste.util.SessionManager;

public class AdminActivity extends AppCompatActivity {
    private Button manageUsersButton, resetDbButton, resetPasswordButton, logoutButton;
    private UserDao userDao;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        userDao = new UserDao(this);
        sessionManager = SessionManager.getInstance(this);

        manageUsersButton = findViewById(R.id.manageUsersButton);
        resetDbButton = findViewById(R.id.resetDbButton);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        logoutButton = findViewById(R.id.logoutButton);

        manageUsersButton.setOnClickListener(v -> startActivity(new Intent(this, UserManagementActivity.class)));
        resetDbButton.setOnClickListener(v -> resetDatabase());
        resetPasswordButton.setOnClickListener(v -> showResetPasswordDialog());
        logoutButton.setOnClickListener(v -> logout());
    }

    private void resetDatabase() {
        new AlertDialog.Builder(this)
                .setTitle("Reset Database")
                .setMessage("This will delete all waiters, recipes, and sales. Continue?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    userDao.resetDatabase();
                    Toast.makeText(this, "Database reset successfully", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showResetPasswordDialog() {
        EditText emailInput = new EditText(this);
        emailInput.setHint("User Email");

        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Reset Password")
                .setView(emailInput)
                .setPositiveButton("Reset", (dialog, which) -> {
                    String email = emailInput.getText().toString().trim();
                    if (email.isEmpty()) {
                        Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String defaultPassword = "waiter-pwd";

                    boolean success = userDao.resetPasswordByEmail(email, defaultPassword);

                    if (success) {
                        Toast.makeText(
                                this,
                                "Password reset to default: " + defaultPassword,
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(
                                this,
                                "User with this email not found",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void logout() {
        sessionManager.clearSession();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}