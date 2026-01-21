package com.utaste.data.repo;

import com.utaste.data.model.*;
import com.utaste.data.network.OpenFoodFactsClient;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryRepository implements AppRepository {
    private final Map<String, User> usersByEmail = new HashMap<>();
    private final Map<Long, Ingredient> ingredients = new HashMap<>();
    private final Map<Long, Recipe> recipes = new HashMap<>();
    private final Map<Long, Sale> sales = new HashMap<>();
    private final AtomicLong idGen = new AtomicLong(1);
    private final OpenFoodFactsClient offClient;

    public InMemoryRepository(OpenFoodFactsClient offClient) {
        this.offClient = offClient;
        seedDefaults();
    }

    private void seedDefaults() {
        usersByEmail.put("admin", new Administrator("admin","admin-pwd","Admin",""));
        usersByEmail.put("chef",  new Chef("chef","chef-pwd","Chef",""));
        usersByEmail.put("alice@waiter", new Waiter("alice@waiter","waiter-pwd","Alice","W."));
        usersByEmail.put("bob@waiter",   new Waiter("bob@waiter","waiter-pwd","Bob","W."));
    }

    @Override public User authenticate(String email, String password) {
        User u = usersByEmail.get(email);
        return (u != null && u.checkPassword(password)) ? u : null;
    }
    @Override public boolean changeOwnPassword(String email, String oldPwd, String newPwd) {
        User u = usersByEmail.get(email);
        if (u == null || !u.checkPassword(oldPwd)) return false;
        u.setPassword(newPwd); return true;
    }
    @Override public boolean resetPassword(String adminEmail, String targetEmail, String newPwd) {
        User admin = usersByEmail.get(adminEmail);
        if (admin == null || admin.role()!=Role.ADMINISTRATOR) return false;
        User target = usersByEmail.get(targetEmail);
        if (target == null) return false;
        target.setPassword(newPwd); return true;
    }

    @Override public Waiter createWaiter(String adminEmail, String email, String first, String last) {
        User admin = usersByEmail.get(adminEmail);
        if (admin == null || admin.role()!=Role.ADMINISTRATOR) return null;
        if (usersByEmail.containsKey(email)) return null;
        Waiter w = new Waiter(email, "waiter-pwd", first, last);
        usersByEmail.put(email, w);
        return w;
    }
    @Override public boolean deleteWaiter(String adminEmail, String email) {
        User admin = usersByEmail.get(adminEmail);
        if (admin == null || admin.role()!=Role.ADMINISTRATOR) return false;
        User removed = usersByEmail.remove(email);
        return removed instanceof Waiter;
    }
    @Override public List<Waiter> listWaiters() {
        List<Waiter> out = new ArrayList<>();
        for (User u : usersByEmail.values()) if (u instanceof Waiter) out.add((Waiter)u);
        return out;
    }
    @Override public boolean updateProfile(String actorEmail, String targetEmail, String first, String last, String newEmail) {
        User actor = usersByEmail.get(actorEmail);
        User target = usersByEmail.get(targetEmail);
        if (actor==null || target==null) return false;
        if (!actorEmail.equals(targetEmail) && actor.role()!=Role.ADMINISTRATOR) return false;
        usersByEmail.remove(targetEmail);
        target.setProfile(first, last, newEmail);
        usersByEmail.put(newEmail, target);
        return true;
    }

    @Override public Recipe createRecipe(String chefEmail, String name, String description) {
        User chef = usersByEmail.get(chefEmail);
        if (chef==null || chef.role()!=Role.CHEF) return null;
        long id = idGen.getAndIncrement();
        Recipe r = new Recipe(id, name);
        r.description = description;
        recipes.put(id, r);
        return r;
    }
    @Override public boolean updateRecipe(String chefEmail, long recipeId, String name, String description) {
        if (!isChef(chefEmail)) return false;
        Recipe r = recipes.get(recipeId); if (r==null) return false;
        r.name = name; r.description = description; return true;
    }
    @Override public boolean deleteRecipe(String chefEmail, long recipeId) {
        if (!isChef(chefEmail)) return false;
        return recipes.remove(recipeId)!=null;
    }
    @Override public boolean addIngredientToRecipe(String chefEmail, long recipeId, long ingredientId, double percent) {
        if (!isChef(chefEmail)) return false;
        Recipe r = recipes.get(recipeId); if (r==null || !ingredients.containsKey(ingredientId)) return false;
        r.parts.add(new RecipeIngredient(recipeId, ingredientId, percent)); return true;
    }
    @Override public boolean updateIngredientPercent(String chefEmail, long recipeId, long ingredientId, double percent) {
        if (!isChef(chefEmail)) return false;
        Recipe r = recipes.get(recipeId); if (r==null) return false;
        for (RecipeIngredient ri : r.parts) if (ri.ingredientId==ingredientId) { ri.percent=percent; return true; }
        return false;
    }
    @Override public boolean removeIngredientFromRecipe(String chefEmail, long recipeId, long ingredientId) {
        if (!isChef(chefEmail)) return false;
        Recipe r = recipes.get(recipeId); if (r==null) return false;
        return r.parts.removeIf(ri -> ri.ingredientId==ingredientId);
    }
    @Override public List<Recipe> listRecipes() { return new ArrayList<>(recipes.values()); }
    @Override public NutritionInfo computeRecipeNutrition(long recipeId) {
        Recipe r = recipes.get(recipeId); if (r==null) return null;
        return r.computeNutrition(ingredients);
    }

    @Override public Ingredient addIngredientFromBarcode(String chefEmail, String barcode, double defaultPercent) {
        if (!isChef(chefEmail)) return null;
        NutritionInfo ni = new NutritionInfo(200, 30, 5, 7, 0, "stub", barcode);
        long id = idGen.getAndIncrement();
        Ingredient ing = new Ingredient(id, "Ingredient "+barcode, ni);
        ingredients.put(id, ing);
        return ing;
    }
    @Override public List<Ingredient> listIngredients() { return new ArrayList<>(ingredients.values()); }

    @Override public Sale recordSale(String waiterEmail, long recipeId, int rating, String note) {
        User w = usersByEmail.get(waiterEmail);
        if (w==null || w.role()!=Role.WAITER || !recipes.containsKey(recipeId)) return null;
        long id = idGen.getAndIncrement();
        Sale s = new Sale(id, recipeId, waiterEmail, rating, note);
        sales.put(id, s); return s;
    }
    @Override public Map<Long, Long> salesCountPerRecipe() {
        Map<Long, Long> m = new HashMap<>();
        for (Sale s : sales.values()) m.merge(s.recipeId, 1L, Long::sum);
        return m;
    }
    @Override public Map<Long, Double> averageRatingPerRecipe() {
        Map<Long, int[]> acc = new HashMap<>();
        for (Sale s : sales.values()) acc.computeIfAbsent(s.recipeId, k->new int[2]);
        for (Sale s : sales.values()) { int[] a = acc.get(s.recipeId); a[0]+=s.rating; a[1]++; }
        Map<Long, Double> out = new HashMap<>();
        for (var e: acc.entrySet()) out.put(e.getKey(), e.getValue()[1]==0?0.0:(e.getValue()[0]*1.0/e.getValue()[1]));
        return out;
    }

    @Override public void resetDatabase(String adminEmail) {
        User admin = usersByEmail.get(adminEmail);
        if (admin==null || admin.role()!=Role.ADMINISTRATOR) return;
        ingredients.clear(); recipes.clear(); sales.clear();
    }

    private boolean isChef(String email) {
        User u = usersByEmail.get(email);
        return u!=null && u.role()==Role.CHEF;
    }
}
