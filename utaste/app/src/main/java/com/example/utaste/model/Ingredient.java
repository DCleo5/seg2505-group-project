package com.example.utaste.model;

public class Ingredient {
    private long id;
    private String name;
    private String barcode;
    private NutritionalInfo nutritionalInfo;

    public Ingredient() {}

    public Ingredient(long id, String name, String barcode, NutritionalInfo nutritionalInfo) {
        this.id = id;
        this.name = name;
        this.barcode = barcode;
        this.nutritionalInfo = nutritionalInfo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public NutritionalInfo getNutritionalInfo() { return nutritionalInfo; }
    public void setNutritionalInfo(NutritionalInfo nutritionalInfo) { this.nutritionalInfo = nutritionalInfo; }
}