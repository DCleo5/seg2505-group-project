package com.utaste.data.model;
public final class Ingredient {
    public final long id;
    public final String name;
    public final NutritionInfo nutrition;
    public Ingredient(long id, String name, NutritionInfo nutrition) {
        this.id = id; this.name = name; this.nutrition = nutrition;
    }
}
