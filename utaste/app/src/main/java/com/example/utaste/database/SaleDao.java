package com.example.utaste.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.utaste.model.RecipeSalesSummary;

import java.util.ArrayList;
import java.util.List;

public class SaleDao {

    private final DatabaseHelper dbHelper;

    public SaleDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public long addSale(long recipeId, long waiterId, int rating, String comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("waiter_id", waiterId);
        values.put("rating", rating);
        values.put("comment", comment);
        values.put("timestamp", System.currentTimeMillis());

        return db.insert("sales", null, values);
    }

    public List<String> getCommentsForRecipe(long recipeId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<String> comments = new ArrayList<>();

        Cursor cursor = db.query(
                "sales",
                new String[] { "comment" },
                "recipe_id = ? AND comment IS NOT NULL AND comment != ''",
                new String[] { String.valueOf(recipeId) },
                null, null, "timestamp DESC");

        while (cursor.moveToNext()) {
            comments.add(cursor.getString(0));
        }

        cursor.close();
        return comments;
    }

    public List<RecipeSalesSummary> getSalesSummary() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        List<RecipeSalesSummary> list = new ArrayList<>();

        Cursor cursor = db.rawQuery(
                "SELECT r.id, r.name, " +
                        "COUNT(s.id) AS sale_count, " +
                        "AVG(s.rating) AS avg_rating " +
                        "FROM recipes r " +
                        "LEFT JOIN sales s ON r.id = s.recipe_id " +
                        "GROUP BY r.id, r.name " +
                        "ORDER BY sale_count DESC",
                null);

        while (cursor.moveToNext()) {
            RecipeSalesSummary summary = new RecipeSalesSummary();
            summary.setRecipeId(cursor.getLong(0));
            summary.setRecipeName(cursor.getString(1));
            summary.setSaleCount(cursor.getInt(2));
            summary.setAvgRating(cursor.isNull(3) ? 0.0 : cursor.getDouble(3));

            list.add(summary);
        }

        cursor.close();
        return list;
    }
}
