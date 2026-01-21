package com.example.utaste.performance;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.utaste.database.RecipeDao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class DatabasePerformanceTest {

    private RecipeDao recipeDao;
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        recipeDao = new RecipeDao(context);
    }

    @Test
    public void testRecipeInsertionPerformance() {
        long startTime = System.currentTimeMillis();

        int numberOfRecipes = 100;
        for (int i = 0; i < numberOfRecipes; i++) {
            recipeDao.createRecipe("Recipe " + i, "Desc", "img", 1);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Assert that inserting 100 recipes takes less than 2000ms (2 seconds)
        // This is a loose threshold, but good for a basic performance sanity check.
        assertTrue("Database insertion took too long: " + duration + "ms", duration < 2000);
    }

    @Test
    public void testRecipeRetrievalPerformance() {
        // Ensure we have data
        for (int i = 0; i < 50; i++) {
            recipeDao.createRecipe("Recipe " + i, "Desc", "img", 1);
        }

        long startTime = System.currentTimeMillis();
        recipeDao.getAllRecipes();
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        // Assert retrieval takes less than 500ms
        assertTrue("Database retrieval took too long: " + duration + "ms", duration < 500);
    }
}
