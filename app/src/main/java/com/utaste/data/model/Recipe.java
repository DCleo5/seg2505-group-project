package com.utaste.data.model;

import java.util.*;

public final class Recipe {
    public final long id;
    public String name;
    public String description;
    public final List<RecipeIngredient> parts = new ArrayList<>();

    public Recipe(long id, String name) { this.id = id; this.name = name; }

    public NutritionInfo computeNutrition(Map<Long, Ingredient> ingredients) {
        double kcal=0, c=0, p=0, f=0, s=0;
        for (RecipeIngredient ri : parts) {
            Ingredient ing = ingredients.get(ri.ingredientId);
            if (ing == null) continue;
            double w = ri.percent / 100.0;
            kcal += w * ing.nutrition.energyKcal;
            c    += w * ing.nutrition.carbsG;
            p    += w * ing.nutrition.proteinG;
            f    += w * ing.nutrition.fatG;
            s    += w * ing.nutrition.saltG;
        }
        return new NutritionInfo(kcal, c, p, f, s, "Computed(uTaste)", null);
    }
}
