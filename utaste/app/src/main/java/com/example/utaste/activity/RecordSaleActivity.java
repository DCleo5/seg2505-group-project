package com.example.utaste.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.database.RecipeDao;
import com.example.utaste.database.SaleDao;
import com.example.utaste.model.Recipe;
import com.example.utaste.model.User;
import com.example.utaste.util.SessionManager;

public class RecordSaleActivity extends AppCompatActivity {

    private TextView recipeNameText;
    private RatingBar ratingBar;
    private EditText commentInput;
    private Button saveSaleButton;

    private RecipeDao recipeDao;
    private SaleDao saleDao;
    private SessionManager sessionManager;

    private long recipeId;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_sale);

        recipeId = getIntent().getLongExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipeDao = new RecipeDao(this);
        saleDao = new SaleDao(this);
        sessionManager = SessionManager.getInstance(this);

        recipe = recipeDao.getRecipeById(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        setupListeners();
    }

    private void initViews() {
        recipeNameText = findViewById(R.id.recipeNameText);
        ratingBar = findViewById(R.id.ratingBar);
        commentInput = findViewById(R.id.commentInput);
        saveSaleButton = findViewById(R.id.saveSaleButton);

        recipeNameText.setText(recipe.getName());
        ratingBar.setRating(5);
    }

    private void setupListeners() {
        saveSaleButton.setOnClickListener(v -> saveSale());
    }

    private void saveSale() {
        int rating = (int) ratingBar.getRating();
        String comment = commentInput.getText().toString().trim();

        if (rating <= 0) {
            Toast.makeText(this, "Please set a rating", Toast.LENGTH_SHORT).show();
            return;
        }

        User waiter = sessionManager.getCurrentUser();
        if (waiter == null) {
            Toast.makeText(this, "No logged-in waiter", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = saleDao.addSale(recipeId, waiter.getId(), rating, comment);
        if (result > 0) {
            Toast.makeText(this, "Sale saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save sale", Toast.LENGTH_SHORT).show();
        }
    }
}
