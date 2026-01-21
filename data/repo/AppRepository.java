package com.utaste.data.repo;

import androidx.annotation.Nullable;
import com.utaste.data.model.*;
import java.util.*;

public interface AppRepository {
    @Nullable User authenticate(String email, String password);
    boolean changeOwnPassword(String email, String oldPwd, String newPwd);
    boolean resetPassword(String adminEmail, String targetEmail, String newPwd);

    Waiter createWaiter(String adminEmail, String email, String first, String last);
    boolean deleteWaiter(String adminEmail, String email);
    List<Waiter> listWaiters();
    boolean updateProfile(String actorEmail, String targetEmail, String first, String last, String newEmail);

    Recipe createRecipe(String chefEmail, String name, String description);
    boolean updateRecipe(String chefEmail, long recipeId, String name, String description);
    boolean deleteRecipe(String chefEmail, long recipeId);
    boolean addIngredientToRecipe(String chefEmail, long recipeId, long ingredientId, double percent);
    boolean updateIngredientPercent(String chefEmail, long recipeId, long ingredientId, double percent);
    boolean removeIngredientFromRecipe(String chefEmail, long recipeId, long ingredientId);
    List<Recipe> listRecipes();
    NutritionInfo computeRecipeNutrition(long recipeId);

    Ingredient addIngredientFromBarcode(String chefEmail, String barcode, double defaultPercent);
    List<Ingredient> listIngredients();

    Sale recordSale(String waiterEmail, long recipeId, int rating, String note);
    Map<Long, Long> salesCountPerRecipe();
    Map<Long, Double> averageRatingPerRecipe();

    void resetDatabase(String adminEmail);
}
