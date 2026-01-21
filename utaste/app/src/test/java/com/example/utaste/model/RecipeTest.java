package com.example.utaste.model;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.ArrayList;

public class RecipeTest {

    @Test
    public void testRecipeCreation() {
        Recipe recipe = new Recipe(1, "Pasta", "Delicious pasta", "image_path", 100);

        assertEquals(1, recipe.getId());
        assertEquals("Pasta", recipe.getName());
        assertEquals("Delicious pasta", recipe.getDescription());
        assertEquals("image_path", recipe.getImagePath());
        assertEquals(100, recipe.getChefId());
        assertNotNull(recipe.getCreatedAt());
        assertNotNull(recipe.getIngredients());
    }

    @Test
    public void testSetters() {
        Recipe recipe = new Recipe();
        recipe.setName("Pizza");
        recipe.setDescription("Yummy");
        recipe.setChefId(200);

        assertEquals("Pizza", recipe.getName());
        assertEquals("Yummy", recipe.getDescription());
        assertEquals(200, recipe.getChefId());
    }

    // Robustness / Boundary Tests

    @Test
    public void testNullName() {
        Recipe recipe = new Recipe(1, null, "Desc", "img", 100);
        assertNull(recipe.getName());
    }

    @Test
    public void testEmptyIngredients() {
        Recipe recipe = new Recipe();
        assertEquals(0, recipe.getIngredients().size());
    }

    @Test
    public void testCalculateNutritionalInfoEmpty() {
        Recipe recipe = new Recipe();
        NutritionalInfo info = recipe.calculateNutritionalInfo();
        assertNotNull(info);
        assertEquals(0.0, info.getCalories(), 0.001);
    }
}
