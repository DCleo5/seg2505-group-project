package com.utaste.data.model;

import java.util.Objects;

public final class NutritionInfo {
    public final double energyKcal;
    public final double carbsG;
    public final double proteinG;
    public final double fatG;
    public final double saltG;
    public final String source;
    public final String barcode;

    public NutritionInfo(double kcal, double carbsG, double proteinG, double fatG, double saltG,
                         String source, String barcode) {
        this.energyKcal = kcal; this.carbsG = carbsG; this.proteinG = proteinG;
        this.fatG = fatG; this.saltG = saltG;
        this.source = Objects.requireNonNull(source);
        this.barcode = barcode;
    }
}
