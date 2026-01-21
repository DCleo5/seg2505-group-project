package com.example.utaste.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.adapter.SellerRecipeAdapter;
import com.example.utaste.database.RecipeDao;
import com.example.utaste.model.Recipe;

import java.util.List;

public class SellerRecipesActivity extends AppCompatActivity {

    private ListView listView;
    private RecipeDao recipeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_recipes);

        listView = findViewById(R.id.sellerRecipesListView);
        recipeDao = new RecipeDao(this);

        List<Recipe> recipes = recipeDao.getAllRecipes();
        if (recipes == null || recipes.isEmpty()) {
            Toast.makeText(this, "No recipes available", Toast.LENGTH_SHORT).show();
            return;
        }

        SellerRecipeAdapter adapter = new SellerRecipeAdapter(
                this,
                recipes,
                recipe -> {
                    Intent i = new Intent(SellerRecipesActivity.this, RecordSaleActivity.class);
                    i.putExtra("RECIPE_ID", recipe.getId());
                    startActivity(i);
                }
        );

        listView.setAdapter(adapter);
    }
}
