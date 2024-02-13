package com.example.proiectdatabase;

public class MovieReview {
    private String username;
    private int rating;
    private String comment;

    public MovieReview(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    // Getters and Setters for the fields (username, rating, comment)
    // You can generate these methods using your IDE or manually add them
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
