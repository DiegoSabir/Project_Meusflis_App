package com.example.meusflis.model;

public class MultimediaContent {
    public String title, genre, author, demographicCategories;
    public byte[] cover;
    public int like;

    public MultimediaContent() {
    }

    public MultimediaContent(String title, String genre, String author, String demographicCategories, byte[] cover, int like) {
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.demographicCategories = demographicCategories;
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

    public String getDemographicCategories() {
        return demographicCategories;
    }

    public void setDemographicCategories(String demographicCategories) {
        this.demographicCategories = demographicCategories;
    }

    public byte[] getCover() {
        return cover;
    }

    public void setCover(byte[] cover) {
        this.cover = cover;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
