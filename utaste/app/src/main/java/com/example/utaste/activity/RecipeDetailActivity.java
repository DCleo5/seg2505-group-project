package com.example.utaste.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.database.RecipeDao;
import com.example.utaste.model.Recipe;

import java.io.File;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView recipeNameText;
    private TextView recipeDescriptionText;
    private TextView nutritionalInfoText;
    private ImageView recipeImageView;

    private RecipeDao recipeDao;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        recipeDao = new RecipeDao(this);

        long recipeId = getIntent().getLongExtra("RECIPE_ID", -1);
        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipe = recipeDao.getRecipeById(recipeId);
        if (recipe == null) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        initViews();
        bindRecipeToViews();
    }

    private void initViews() {
        recipeNameText = findViewById(R.id.recipeNameText);
        recipeDescriptionText = findViewById(R.id.recipeDescriptionText);
        nutritionalInfoText = findViewById(R.id.nutritionalInfoText);
        recipeImageView = findViewById(R.id.recipeImageView);
    }

    private void bindRecipeToViews() {
        recipeNameText.setText(recipe.getName());

        String fullDesc = recipe.getDescription();
        if (fullDesc == null) fullDesc = "";

        String ingredientsPart = fullDesc;
        String nutritionPart = null;

        String marker = "Nutritional info:\n";
        int idx = fullDesc.indexOf(marker);
        if (idx >= 0) {
            ingredientsPart = fullDesc.substring(0, idx).trim();
            nutritionPart = fullDesc.substring(idx + marker.length()).trim();
        }

        if (ingredientsPart.isEmpty()) {
            recipeDescriptionText.setText("No ingredients description saved.");
        } else {
            recipeDescriptionText.setText(ingredientsPart);
        }

        if (nutritionPart == null || nutritionPart.isEmpty()) {
            nutritionalInfoText.setText("No nutritional information calculated yet.");
        } else {
            nutritionalInfoText.setText(nutritionPart);
        }


        String path = recipe.getImagePath();
        if (path != null && !path.isEmpty() && !path.startsWith("content://")) {
            try {
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    recipeImageView.setImageBitmap(bitmap);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
