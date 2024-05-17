package com.example.meusflis;

public class ListElement {
    public String color;
    public String title;
    public String genre;
    public String author;
    public String demographicCategories;

    public ListElement(String color, String title, String genre, String author, String demographicCategories) {
        this.color = color;
        this.title = title;
        this.genre = genre;
        this.author = author;
        this.demographicCategories = demographicCategories;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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
}
