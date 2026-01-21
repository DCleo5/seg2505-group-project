package com.example.utaste.model;

public class RecipeIngredient {
    private long id;
    private long recipeId;
    private long ingredientId;
    private double percentage;
    private Ingredient ingredient;

    public RecipeIngredient() {}

    public RecipeIngredient(long id, long recipeId, long ingredientId, double percentage) {
        this.id = id;
        this.recipeId = recipeId;
        this.ingredientId = ingredientId;
        this.percentage = percentage;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getRecipeId() { return recipeId; }
    public void setRecipeId(long recipeId) { this.recipeId = recipeId; }
    public long getIngredientId() { return ingredientId; }
    public void setIngredientId(long ingredientId) { this.ingredientId = ingredientId; }
    public double getPercentage() { return percentage; }
    public void setPercentage(double percentage) { this.percentage = percentage; }
    public Ingredient getIngredient() { return ingredient; }
    public void setIngredient(Ingredient ingredient) { this.ingredient = ingredient; }
}