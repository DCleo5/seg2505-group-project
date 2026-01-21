package com.example.utaste.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.utaste.R;
import com.example.utaste.adapter.UserAdapter;
import com.example.utaste.database.UserDao;
import com.example.utaste.model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class UserManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private UserDao userDao;
    private FloatingActionButton addUserFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_management);

        userDao = new UserDao(this);
        recyclerView = findViewById(R.id.usersRecyclerView);
        addUserFab = findViewById(R.id.addUserFab);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadUsers();

        addUserFab.setOnClickListener(v -> showAddUserDialog());
    }

    private void loadUsers() {
        List<User> users = userDao.getAllUsers();
        if (adapter == null) {
            adapter = new UserAdapter(users, this::confirmDeleteUser);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.updateList(users);
        }
    }

    private void confirmDeleteUser(User user) {
        new AlertDialog.Builder(this)
                .setTitle("Delete User")
                .setMessage("Are you sure you want to delete " + user.getFirstName() + " " + user.getLastName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (userDao.deleteUser(user.getId())) {
                        Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    } else {
                        Toast.makeText(this, "Failed to delete user", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showAddUserDialog() {

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        final EditText firstNameInput = new EditText(this);
        firstNameInput.setHint("First Name");
        layout.addView(firstNameInput);

        final EditText lastNameInput = new EditText(this);
        lastNameInput.setHint("Last Name");
        layout.addView(lastNameInput);

        final EditText emailInput = new EditText(this);
        emailInput.setHint("Email");
        layout.addView(emailInput);

        final EditText passwordInput = new EditText(this);
        passwordInput.setHint("Password");
        layout.addView(passwordInput);

        final Spinner roleSpinner = new Spinner(this);
        String[] roles = { "WAITER", "CHEF", "ADMINISTRATOR" };
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(adapter);
        layout.addView(roleSpinner);

        new AlertDialog.Builder(this)
                .setTitle("Add New User")
                .setView(layout)
                .setPositiveButton("Add", (dialog, which) -> {
                    String firstName = firstNameInput.getText().toString().trim();
                    String lastName = lastNameInput.getText().toString().trim();
                    String email = emailInput.getText().toString().trim();
                    String password = passwordInput.getText().toString().trim();
                    String role = (String) roleSpinner.getSelectedItem();

                    if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                        Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    long id = userDao.createUser(firstName, lastName, email, password, role);
                    if (id > 0) {
                        Toast.makeText(this, "User created", Toast.LENGTH_SHORT).show();
                        loadUsers();
                    } else {
                        Toast.makeText(this, "Failed to create user", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
