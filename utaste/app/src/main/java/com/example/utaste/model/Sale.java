package com.example.utaste.model;

import java.util.Date;

public class Sale {
    private long id;
    private long recipeId;
    private long waiterId;
    private int rating;
    private String comment;
    private Date timestamp;

    public Sale() {
        this.timestamp = new Date();
    }

    public Sale(long id, long recipeId, long waiterId, int rating, String comment) {
        this.id = id;
        this.recipeId = recipeId;
        this.waiterId = waiterId;
        this.rating = rating;
        this.comment = comment;
        this.timestamp = new Date();
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getRecipeId() { return recipeId; }
    public void setRecipeId(long recipeId) { this.recipeId = recipeId; }
    public long getWaiterId() { return waiterId; }
    public void setWaiterId(long waiterId) { this.waiterId = waiterId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public Date getTimestamp() { return timestamp; }
}