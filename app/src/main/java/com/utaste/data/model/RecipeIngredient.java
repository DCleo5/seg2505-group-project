package com.utaste.data.model;
public final class RecipeIngredient {
    public final long recipeId;
    public final long ingredientId;
    public double percent;
    public RecipeIngredient(long recipeId, long ingredientId, double percent) {
        this.recipeId = recipeId; this.ingredientId = ingredientId; this.percent = percent;
    }
}
