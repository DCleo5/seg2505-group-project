package com.example.utaste;

import android.content.Context;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.utaste.database.DatabaseHelper;
import com.example.utaste.database.RecipeDao;
import com.example.utaste.database.SaleDao;
import com.example.utaste.model.RecipeSalesSummary;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class SaleDaoInstrumentedTest {

    private Context context;
    private DatabaseHelper dbHelper;
    private RecipeDao recipeDao;
    private SaleDao saleDao;

    private long testRecipeId;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        dbHelper = new DatabaseHelper(context);


        SQLiteDatabase db = dbHelper.getWritableDatabase();
        dbHelper.onUpgrade(db, 1, 1);

        recipeDao = new RecipeDao(context);
        saleDao = new SaleDao(context);


        testRecipeId = recipeDao.createRecipe(
                "Test Pizza",
                "Desc test\n\nNutritional info:\nCalories: 300 kcal",
                null,
                1 // chefId
        );
        assertTrue(testRecipeId > 0);
    }

    @Test
    public void testAddSaleAndSummary() {

        List<RecipeSalesSummary> empty = saleDao.getSalesSummary();
        assertNotNull(empty);


        assertTrue(saleDao.addSale(testRecipeId, 2, 5, "Excellent") > 0);
        assertTrue(saleDao.addSale(testRecipeId, 2, 3, "Correct") > 0);
        assertTrue(saleDao.addSale(testRecipeId, 2, 4, "Bon") > 0);

        
        List<RecipeSalesSummary> summaries = saleDao.getSalesSummary();
        assertEquals(1, summaries.size());

        RecipeSalesSummary summary = summaries.get(0);

        assertEquals("Test Pizza", summary.getRecipeName());
        assertEquals(3, summary.getSaleCount());

        double expectedAvg = (5 + 3 + 4) / 3.0;
        assertEquals(expectedAvg, summary.getAvgRating(), 0.01);
    }
}
