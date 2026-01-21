package com.example.utaste.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "uTaste.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "first_name TEXT," +
                "last_name TEXT," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "role TEXT NOT NULL," +
                "created_at INTEGER NOT NULL," +
                "updated_at INTEGER NOT NULL" +
                ")");

        db.execSQL("INSERT INTO users (first_name, last_name, email, password, role, created_at, updated_at) VALUES " +
                "('Admin', 'User', 'admin', 'admin-pwd', 'ADMINISTRATOR', " + System.currentTimeMillis() + ", " + System.currentTimeMillis() + "), " +
                "('Chef', 'User', 'chef', 'chef-pwd', 'CHEF', " + System.currentTimeMillis() + ", " + System.currentTimeMillis() + ")");

        db.execSQL("CREATE TABLE recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT UNIQUE NOT NULL," +
                "description TEXT," +
                "image_path TEXT," +
                "chef_id INTEGER NOT NULL," +
                "created_at INTEGER NOT NULL," +
                "updated_at INTEGER NOT NULL," +
                "FOREIGN KEY(chef_id) REFERENCES users(id)" +
                ")");

        db.execSQL("CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT NOT NULL," +
                "barcode TEXT UNIQUE," +
                "nutritional_data TEXT" +
                ")");

        db.execSQL("CREATE TABLE recipe_ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "recipe_id INTEGER NOT NULL," +
                "ingredient_id INTEGER NOT NULL," +
                "percentage REAL NOT NULL," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE," +
                "FOREIGN KEY(ingredient_id) REFERENCES ingredients(id) ON DELETE CASCADE," +
                "UNIQUE(recipe_id, ingredient_id)" +
                ")");

        db.execSQL("CREATE TABLE sales (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "recipe_id INTEGER NOT NULL," +
                "waiter_id INTEGER NOT NULL," +
                "rating INTEGER NOT NULL," +
                "comment TEXT," +
                "timestamp INTEGER NOT NULL," +
                "FOREIGN KEY(recipe_id) REFERENCES recipes(id) ON DELETE CASCADE," +
                "FOREIGN KEY(waiter_id) REFERENCES users(id)" +
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS sales");
        db.execSQL("DROP TABLE IF EXISTS recipe_ingredients");
        db.execSQL("DROP TABLE IF EXISTS ingredients");
        db.execSQL("DROP TABLE IF EXISTS recipes");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }
}