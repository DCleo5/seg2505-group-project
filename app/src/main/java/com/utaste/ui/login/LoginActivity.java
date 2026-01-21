package com.utaste.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.utaste.R;
import com.utaste.data.model.Role;
import com.utaste.data.model.User;
import com.utaste.data.network.OpenFoodFactsClient;
import com.utaste.data.repo.AppRepository;
import com.utaste.data.repo.InMemoryRepository;
import com.utaste.ui.admin.AdminHomeActivity;
import com.utaste.ui.chef.ChefHomeActivity;
import com.utaste.ui.waiter.WaiterHomeActivity;

public class LoginActivity extends AppCompatActivity {
    private AppRepository repo;
    private EditText etEmail, etPassword;
    private CheckBox cbRemember;

    @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        Button btnLogin = findViewById(R.id.btnLogin);

        repo = new InMemoryRepository(new OpenFoodFactsClient());

        SharedPreferences sp = getSharedPreferences("utaste_prefs", Context.MODE_PRIVATE);
        etEmail.setText(sp.getString("remember_email", ""));

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pwd   = etPassword.getText().toString();
            User u = repo.authenticate(email, pwd);
            if (u == null) { Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show(); return; }
            if (cbRemember.isChecked()) sp.edit().putString("remember_email", email).apply();
            Toast.makeText(this, "Welcome to uTaste", Toast.LENGTH_LONG).show();
            Intent next;
            if (u.role()== Role.ADMINISTRATOR) next = new Intent(this, AdminHomeActivity.class);
            else if (u.role()== Role.CHEF)     next = new Intent(this, ChefHomeActivity.class);
            else                               next = new Intent(this, WaiterHomeActivity.class);
            next.putExtra("user_email", u.getEmail());
            startActivity(next);
        });
    }
}
