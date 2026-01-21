package com.example.utaste.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.utaste.model.Ingredient;
import com.example.utaste.model.NutritionalInfo;
import com.google.gson.Gson;

public class IngredientDao {
    private DatabaseHelper dbHelper;
    private Gson gson;

    public IngredientDao(Context context) {
        this.dbHelper = new DatabaseHelper(context);
        this.gson = new Gson();
    }

    public long addIngredient(String name, String barcode, NutritionalInfo nutritionalInfo) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("barcode", barcode);
        values.put("nutritional_data", gson.toJson(nutritionalInfo));

        long existingId = getIngredientIdByBarcode(barcode);
        if (existingId != -1) {
            db.update("ingredients", values, "id = ?",
                    new String[]{String.valueOf(existingId)});
            return existingId;
        } else {
            return db.insert("ingredients", null, values);
        }
    }

    public Ingredient getIngredientByBarcode(String barcode) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("ingredients", null, "barcode = ?",
                new String[]{barcode}, null, null, null);

        if (cursor.moveToFirst()) {
            Ingredient ingredient = createIngredientFromCursor(cursor);
            cursor.close();
            return ingredient;
        }
        cursor.close();
        return null;
    }

    public long getIngredientIdByBarcode(String barcode) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("ingredients", new String[]{"id"}, "barcode = ?",
                new String[]{barcode}, null, null, null);

        if (cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1;
    }

    private Ingredient createIngredientFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow("id"));
        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String barcode = cursor.getString(cursor.getColumnIndexOrThrow("barcode"));
        String nutritionalData = cursor.getString(cursor.getColumnIndexOrThrow("nutritional_data"));

        NutritionalInfo info = gson.fromJson(nutritionalData, NutritionalInfo.class);
        return new Ingredient(id, name, barcode, info);
    }
}