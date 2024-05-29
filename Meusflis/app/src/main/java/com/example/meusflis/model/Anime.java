package com.example.meusflis.model;

import android.graphics.Bitmap;

public class Anime extends MultimediaContent{
    String status;
    String season;

    public Anime(String title, String genre, String author, String demographicCategory, Bitmap cover, int like) {
        super(title, genre, author, demographicCategory, cover, like);
    }

    public Anime(String title, String genre, String author, String demographicCategory, Bitmap cover, int like, String status, String season) {
        super(title, genre, author, demographicCategory, cover, like);
        this.status = status;
        this.season = season;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }
}
