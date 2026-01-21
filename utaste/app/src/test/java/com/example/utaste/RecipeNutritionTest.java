package com.example.utaste;

import com.example.utaste.model.Ingredient;
import com.example.utaste.model.NutritionalInfo;
import com.example.utaste.model.Recipe;
import com.example.utaste.model.RecipeIngredient;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class RecipeNutritionTest {

    @Test
    public void calculateNutritionalInfo_withTwoIngredients_returnsWeightedSum() {

        NutritionalInfo nut1 = new NutritionalInfo(10.0, 5.0, 2.0, 100.0, 1.0, 0.5);
        Ingredient ing1 = new Ingredient(1L, "Ingredient 1", "1111111111111", nut1);


        NutritionalInfo nut2 = new NutritionalInfo(20.0, 3.0, 4.0, 200.0, 2.0, 1.0);
        Ingredient ing2 = new Ingredient(2L, "Ingredient 2", "2222222222222", nut2);


        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.setIngredient(ing1);
        ri1.setPercentage(40.0);

        RecipeIngredient ri2 = new RecipeIngredient();
        ri2.setIngredient(ing2);
        ri2.setPercentage(60.0);

        List<RecipeIngredient> list = new ArrayList<>();
        list.add(ri1);
        list.add(ri2);

        Recipe recipe = new Recipe();
        recipe.setIngredients(list);


        double expectedCarb   = 10.0 * 0.40 + 20.0 * 0.60;   // 16.0
        double expectedProt   = 5.0  * 0.40 + 3.0  * 0.60;   // 3.8
        double expectedLipids = 2.0  * 0.40 + 4.0  * 0.60;   // 3.2
        double expectedCal    = 100.0* 0.40 + 200.0* 0.60;   // 160.0
        double expectedFibers = 1.0  * 0.40 + 2.0  * 0.60;   // 1.6
        double expectedSalt   = 0.5  * 0.40 + 1.0  * 0.60;   // 0.8

        NutritionalInfo result = recipe.calculateNutritionalInfo();

        double delta = 1e-6;
        assertEquals(expectedCarb,   result.getCarbohydrates(), delta);
        assertEquals(expectedProt,   result.getProteins(),      delta);
        assertEquals(expectedLipids, result.getLipids(),        delta);
        assertEquals(expectedCal,    result.getCalories(),      delta);
        assertEquals(expectedFibers, result.getFibers(),        delta);
        assertEquals(expectedSalt,   result.getSalt(),          delta);
    }

    @Test
    public void calculateNutritionalInfo_withNoIngredient_returnsZeros() {
        Recipe recipe = new Recipe();
        recipe.setIngredients(new ArrayList<>());

        NutritionalInfo result = recipe.calculateNutritionalInfo();

        double delta = 1e-6;
        assertEquals(0.0, result.getCarbohydrates(), delta);
        assertEquals(0.0, result.getProteins(),      delta);
        assertEquals(0.0, result.getLipids(),        delta);
        assertEquals(0.0, result.getCalories(),      delta);
        assertEquals(0.0, result.getFibers(),        delta);
        assertEquals(0.0, result.getSalt(),          delta);
    }
}
