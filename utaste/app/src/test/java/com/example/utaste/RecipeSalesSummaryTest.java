package com.example.utaste;

import com.example.utaste.model.RecipeSalesSummary;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecipeSalesSummaryTest {

    @Test
    public void testGettersSetters() {
        RecipeSalesSummary summary = new RecipeSalesSummary();

        summary.setRecipeId(10);
        summary.setRecipeName("Pizza Test");
        summary.setSaleCount(5);
        summary.setAvgRating(4.6);

        assertEquals(10, summary.getRecipeId());
        assertEquals("Pizza Test", summary.getRecipeName());
        assertEquals(5, summary.getSaleCount());
        assertEquals(4.6, summary.getAvgRating(), 0.001);
    }

    @Test
    public void testDefaultValues() {
        RecipeSalesSummary summary = new RecipeSalesSummary();

        assertEquals(0, summary.getRecipeId());
        assertNull(summary.getRecipeName());
        assertEquals(0, summary.getSaleCount());
        assertEquals(0.0, summary.getAvgRating(), 0.001);
    }
}
