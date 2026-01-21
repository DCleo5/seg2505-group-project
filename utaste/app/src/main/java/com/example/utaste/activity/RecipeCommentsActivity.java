package com.example.utaste.activity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.utaste.R;
import com.example.utaste.database.SaleDao;

import java.util.List;

public class RecipeCommentsActivity extends AppCompatActivity {

    private TextView recipeTitleText;
    private ListView commentsListView;
    private SaleDao saleDao;
    private long recipeId;
    private String recipeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_comments);

        recipeId = getIntent().getLongExtra("RECIPE_ID", -1);
        recipeName = getIntent().getStringExtra("RECIPE_NAME");

        if (recipeId == -1) {
            Toast.makeText(this, "Invalid recipe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        recipeTitleText = findViewById(R.id.recipeTitleText);
        commentsListView = findViewById(R.id.commentsListView);
        saleDao = new SaleDao(this);

        recipeTitleText.setText("Comments for " + (recipeName != null ? recipeName : "Recipe"));

        loadComments();
    }

    private void loadComments() {
        List<String> comments = saleDao.getCommentsForRecipe(recipeId);

        if (comments == null || comments.isEmpty()) {
            Toast.makeText(this, "No comments found for this recipe.", Toast.LENGTH_SHORT).show();
        } else {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, comments);
            commentsListView.setAdapter(adapter);
        }
    }
}
