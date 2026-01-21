package com.example.utaste.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.database.RecipeDao;
import com.example.utaste.model.Recipe;
import com.example.utaste.util.SessionManager;

import java.util.List;

public class WaiterActivity extends AppCompatActivity {

    private ListView recipeListView;
    private Button viewSalesButton, logoutButton, sellerRecipesButton;
    private RecipeDao recipeDao;
    private SessionManager sessionManager;
    private ArrayAdapter<Recipe> adapter;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter);

        recipeDao = new RecipeDao(this);
        sessionManager = SessionManager.getInstance(this);


        recipeListView = findViewById(R.id.recipeList);
        viewSalesButton = findViewById(R.id.viewSalesButton);
        sellerRecipesButton = findViewById(R.id.sellerRecipesButton);
        logoutButton = findViewById(R.id.logoutButton);


        recipes = recipeDao.getAllRecipes();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recipes);
        recipeListView.setAdapter(adapter);


        recipeListView.setOnItemClickListener((parent, view, position, id) -> {
            Recipe recipe = recipes.get(position);
            Intent intent = new Intent(this, RecordSaleActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            startActivity(intent);
        });


        viewSalesButton.setOnClickListener(v ->
                startActivity(new Intent(this, SalesReportActivity.class))
        );


        sellerRecipesButton.setOnClickListener(v ->
                startActivity(new Intent(this, SellerRecipesActivity.class))
        );


        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        sessionManager.clearSession();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
