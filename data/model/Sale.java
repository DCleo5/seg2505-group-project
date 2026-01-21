package com.utaste.data.model;

import java.time.Instant;

public final class Sale {
    public final long id;
    public final long recipeId;
    public final String waiterEmail;
    public final int rating;
    public final String note;
    public final Instant at;

    public Sale(long id, long recipeId, String waiterEmail, int rating, String note) {
        this.id = id; this.recipeId = recipeId; this.waiterEmail = waiterEmail;
        this.rating = rating; this.note = note; this.at = Instant.now();
    }
}
