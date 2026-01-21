package com.example.utaste.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.utaste.R;
import com.example.utaste.model.Recipe;

import java.io.File;
import java.util.List;

public class SellerRecipeAdapter extends ArrayAdapter<Recipe> {

    public interface OnRecipeClickListener {
        void onRecipeClick(Recipe recipe);
    }

    private final LayoutInflater inflater;
    private final OnRecipeClickListener listener;

    public SellerRecipeAdapter(Context context,
                               List<Recipe> recipes,
                               OnRecipeClickListener listener) {
        super(context, 0, recipes);
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder h;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_seller_recipe, parent, false);
            h = new ViewHolder();
            h.name = convertView.findViewById(R.id.recipeNameText);
            h.nutrition = convertView.findViewById(R.id.recipeNutritionText);
            h.image = convertView.findViewById(R.id.recipeImage);
            convertView.setTag(h);
        } else {
            h = (ViewHolder) convertView.getTag();
        }

        Recipe recipe = getItem(position);
        if (recipe == null) return convertView;

        // Nom
        h.name.setText(recipe.getName() != null ? recipe.getName() : "Unknown recipe");


        String nutritionText = extractNutritionBlock(recipe.getDescription());
        h.nutrition.setText(nutritionText);


        h.image.setImageDrawable(null);
        String path = recipe.getImagePath();
        if (path != null && !path.isEmpty() && !path.startsWith("content://")) {
            try {
                File imgFile = new File(path);
                if (imgFile.exists()) {
                    Bitmap bmp = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    h.image.setImageBitmap(bmp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        convertView.setOnClickListener(v -> {
            if (listener != null) listener.onRecipeClick(recipe);
        });

        return convertView;
    }

    private String extractNutritionBlock(String description) {
        if (description == null || description.isEmpty()) {
            return "No nutritional information available";
        }
        String marker = "Nutritional info:";
        int idx = description.indexOf(marker);
        if (idx >= 0) {
            return description.substring(idx).trim();
        }
        return description.trim();
    }

    static class ViewHolder {
        TextView name;
        TextView nutrition;
        ImageView image;
    }
}
