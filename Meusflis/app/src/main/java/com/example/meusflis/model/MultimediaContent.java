package com.example.meusflis.model;

import android.graphics.Bitmap;

public class MultimediaContent {
    private String title;
    private String genre;
    private String author;
    private String demographicCategory;
    private Bitmap cover;
    private int like;

    public MultimediaContent(String title, String genre, String author, String demographicCategory, Bitmap cover, int like) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.demographicCategory = demographicCategory;
        this.cover = cover;
        this.like = like;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDemographicCategory() {
        return demographicCategory;
    }

    public void setDemographicCategory(String demographicCategory) {
        this.demographicCategory = demographicCategory;
    }

    public Bitmap getCover() {
        return cover;
    }

    public void setCover(Bitmap cover) {
        this.cover = cover;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
