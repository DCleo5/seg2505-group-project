package com.example.utaste.database;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.utaste.model.Recipe;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class RecipeDaoIntegrationTest {

    private RecipeDao recipeDao;
    private UserDao userDao; // Need to clean up users too if needed, but recipeDao doesn't depend on user
                             // table constraints in this simple impl
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        recipeDao = new RecipeDao(context);
        userDao = new UserDao(context);
        userDao.resetDatabase(); // Clean everything
    }

    @After
    public void tearDown() {
        userDao.resetDatabase();
    }

    @Test
    public void testCreateAndGetRecipe() {
        long id = recipeDao.createRecipe("Pasta", "Yummy", "img", 1);
        assertTrue(id > 0);

        Recipe recipe = recipeDao.getRecipeById(id);
        assertNotNull(recipe);
        assertEquals("Pasta", recipe.getName());
        assertEquals("Yummy", recipe.getDescription());
    }

    @Test
    public void testUpdateRecipe() {
        long id = recipeDao.createRecipe("Pizza", "Cheese", "img", 1);
        boolean updated = recipeDao.updateRecipe(id, "Pizza Updated", "More Cheese", "img_new");
        assertTrue(updated);

        Recipe recipe = recipeDao.getRecipeById(id);
        assertEquals("Pizza Updated", recipe.getName());
        assertEquals("More Cheese", recipe.getDescription());
    }

    @Test
    public void testDeleteRecipe() {
        long id = recipeDao.createRecipe("Soup", "Hot", "img", 1);
        boolean deleted = recipeDao.deleteRecipe(id);
        assertTrue(deleted);

        Recipe recipe = recipeDao.getRecipeById(id);
        assertNull(recipe);
    }

    @Test
    public void testGetAllRecipes() {
        recipeDao.createRecipe("R1", "D1", "I1", 1);
        recipeDao.createRecipe("R2", "D2", "I2", 1);

        List<Recipe> recipes = recipeDao.getAllRecipes();
        assertTrue(recipes.size() >= 2);
    }
}
