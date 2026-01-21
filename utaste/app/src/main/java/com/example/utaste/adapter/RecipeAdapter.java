package com.example.utaste.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.utaste.R;
import com.example.utaste.model.Recipe;

import java.util.List;

public class RecipeAdapter extends ArrayAdapter<Recipe> {
    private final Context context; // Made final
    private final List<Recipe> recipes; // Made final

    public RecipeAdapter(@NonNull Context context, @NonNull List<Recipe> recipes) {
        super(context, R.layout.item_recipe, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        }

        Recipe recipe = recipes.get(position);
        TextView nameText = convertView.findViewById(R.id.recipeName);
        TextView descText = convertView.findViewById(R.id.recipeDescription);

        nameText.setText(recipe.getName());
        descText.setText(recipe.getDescription());

        return convertView;
    }
}