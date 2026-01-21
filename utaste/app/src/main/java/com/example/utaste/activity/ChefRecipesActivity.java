package com.example.utaste.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.adapter.RecipeAdapter;
import com.example.utaste.database.RecipeDao;
import com.example.utaste.model.Recipe;
import com.example.utaste.model.User;
import com.example.utaste.util.SessionManager;

import java.util.ArrayList;
import java.util.List;

public class ChefRecipesActivity extends AppCompatActivity {

    private ListView recipeListView;
    private Button addRecipeButton;
    private Button logoutButton;          // 🔹 ajouté
    private RecipeDao recipeDao;
    private SessionManager sessionManager;
    private RecipeAdapter adapter;
    private List<Recipe> recipes;
    private User currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_recipes);

        recipeDao = new RecipeDao(this);
        sessionManager = SessionManager.getInstance(this);
        currentUser = sessionManager.getCurrentUser();

        // Si jamais la session est vide, on renvoie au login
        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }

        recipeListView = findViewById(R.id.recipeList);
        addRecipeButton = findViewById(R.id.addRecipeButton);
        logoutButton = findViewById(R.id.logoutButton);   // 🔹 on récupère le bouton

        recipes = new ArrayList<>();

        adapter = new RecipeAdapter(this, recipes);
        recipeListView.setAdapter(adapter);
        recipeListView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

        recipeListView.setOnItemClickListener((parent, view, position, id) -> {
            Recipe selected = recipes.get(position);
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", selected.getId());
            startActivity(intent);
        });

        addRecipeButton.setOnClickListener(v ->
                startActivity(new Intent(this, AddRecipeActivity.class))
        );


        logoutButton.setOnClickListener(v -> {
            sessionManager.clearSession();

            Intent intent = new Intent(this, LoginActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        refresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        recipes.clear();
        recipes.addAll(recipeDao.getRecipesByChef(currentUser.getId()));
        adapter.notifyDataSetChanged();
    }
}
