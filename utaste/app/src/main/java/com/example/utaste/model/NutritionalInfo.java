package com.example.utaste.model;

public class NutritionalInfo {
    private double carbohydrates;
    private double proteins;
    private double lipids;
    private double calories;
    private double fibers;
    private double salt;

    public NutritionalInfo() {}

    public NutritionalInfo(double carbohydrates, double proteins, double lipids, double calories) {
        this.carbohydrates = carbohydrates;
        this.proteins = proteins;
        this.lipids = lipids;
        this.calories = calories;
    }

    public NutritionalInfo(double carbohydrates, double proteins, double lipids, double calories, double fibers, double salt) {
        this(carbohydrates, proteins, lipids, calories);
        this.fibers = fibers;
        this.salt = salt;
    }

    public double getCarbohydrates() { return carbohydrates; }
    public double getProteins() { return proteins; }
    public double getLipids() { return lipids; }
    public double getCalories() { return calories; }
    public double getFibers() { return fibers; }
    public double getSalt() { return salt; }
}