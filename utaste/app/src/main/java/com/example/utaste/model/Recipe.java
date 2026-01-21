package com.example.utaste.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Recipe {
    private long id;
    private String name;
    private String description;
    private String imagePath;
    private long chefId;
    private Date createdAt;
    private Date updatedAt;
    private List<RecipeIngredient> ingredients;

    public Recipe(long id, String name, String description,
                  String imagePath, long chefId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.imagePath = imagePath;
        this.chefId = chefId;
        this.createdAt = new Date();
        this.ingredients = new ArrayList<>();
    }

    public Recipe() {
        this.ingredients = new ArrayList<>();
        this.createdAt = new Date();
    }


    public NutritionalInfo calculateNutritionalInfo() {
        double totalCarb = 0.0;
        double totalProt = 0.0;
        double totalLip  = 0.0;
        double totalCal  = 0.0;
        double totalFib  = 0.0;
        double totalSalt = 0.0;

        if (ingredients == null) {
            return new NutritionalInfo();
        }

        for (RecipeIngredient ri : ingredients) {
            if (ri == null || ri.getIngredient() == null ||
                    ri.getIngredient().getNutritionalInfo() == null) {
                continue;
            }

            NutritionalInfo n = ri.getIngredient().getNutritionalInfo();
            double factor = ri.getPercentage() / 100.0;

            totalCarb += n.getCarbohydrates() * factor;
            totalProt += n.getProteins()      * factor;
            totalLip  += n.getLipids()        * factor;
            totalCal  += n.getCalories()      * factor;
            totalFib  += n.getFibers()        * factor;
            totalSalt += n.getSalt()          * factor;
        }

        return new NutritionalInfo(
                totalCarb,
                totalProt,
                totalLip,
                totalCal,
                totalFib,
                totalSalt
        );
    }

    // getters / setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public long getChefId() { return chefId; }
    public void setChefId(long chefId) { this.chefId = chefId; }
    public Date getCreatedAt() { return createdAt; }
    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
    public List<RecipeIngredient> getIngredients() { return ingredients; }
    public void setIngredients(List<RecipeIngredient> ingredients) { this.ingredients = ingredients; }

    @Override
    public String toString() {
        return name; // ou getName() si tu préfères
    }

}
