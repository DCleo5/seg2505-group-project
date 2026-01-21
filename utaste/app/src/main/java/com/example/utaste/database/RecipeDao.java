package com.example.utaste.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.utaste.model.Recipe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeDao {
    private DatabaseHelper dbHelper;

    public RecipeDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    public long createRecipe(String name, String description, String imagePath, long chefId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("image_path", imagePath);
        values.put("chef_id", chefId);
        values.put("created_at", System.currentTimeMillis());
        values.put("updated_at", System.currentTimeMillis());
        return db.insert("recipes", null, values);
    }

    public boolean updateRecipe(long id, String name, String description, String imagePath) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        values.put("image_path", imagePath);
        values.put("updated_at", System.currentTimeMillis());
        return db.update("recipes", values, "id = ?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteRecipe(long id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        return db.delete("recipes", "id = ?",
                new String[]{String.valueOf(id)}) > 0;
    }

    public List<Recipe> getAllRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("recipes", null, null, null, null, null, "name ASC");

        while (cursor.moveToNext()) {
            recipes.add(createRecipeFromCursor(cursor));
        }
        cursor.close();
        return recipes;
    }

    public List<Recipe> getRecipesByChef(long chefId) {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("recipes", null, "chef_id = ?",
                new String[]{String.valueOf(chefId)}, null, null, "name ASC");

        while (cursor.moveToNext()) {
            recipes.add(createRecipeFromCursor(cursor));
        }
        cursor.close();
        return recipes;
    }

    public Recipe getRecipeById(long id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("recipes", null, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if (cursor.moveToFirst()) {
            Recipe recipe = createRecipeFromCursor(cursor);
            cursor.close();
            return recipe;
        }
        cursor.close();
        return null;
    }

    private Recipe createRecipeFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        String imagePath = cursor.getString(cursor.getColumnIndexOrThrow("image_path"));
        long chefId = cursor.getLong(cursor.getColumnIndexOrThrow("chef_id"));
        return new Recipe(id, name, description, imagePath, chefId);
    }
}