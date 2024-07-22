package com.example.meusflis.Models;

public class MoviesModel {
    String imageMovie, idMovie, titleMovie, timeMovie, sinopsisMovie, videoMovie, genderMovie, categoryMovie;

    public MoviesModel() {
    }

    public MoviesModel(String imageMovie, String idMovie, String titleMovie, String timeMovie, String sinopsisMovie, String videoMovie, String categoryMovie, String genderMovie) {
        this.imageMovie = imageMovie;
        this.idMovie = idMovie;
        this.titleMovie = titleMovie;
        this.timeMovie = timeMovie;
        this.sinopsisMovie = sinopsisMovie;
        this.videoMovie = videoMovie;
        this.categoryMovie = categoryMovie;
        this.genderMovie = genderMovie;
    }

    public String getImageMovie() {
        return imageMovie;
    }

    public void setImageMovie(String imageMovie) {
        this.imageMovie = imageMovie;
    }

    public String getIdMovie() {
        return idMovie;
    }

    public void setIdMovie(String idMovie) {
        this.idMovie = idMovie;
    }

    public String getTitleMovie() {
        return titleMovie;
    }

    public void setTitleMovie(String titleMovie) {
        this.titleMovie = titleMovie;
    }

    public String getTimeMovie() {
        return timeMovie;
    }

    public void setTimeMovie(String timeMovie) {
        this.timeMovie = timeMovie;
    }

    public String getSinopsisMovie() {
        return sinopsisMovie;
    }

    public void setSinopsisMovie(String sinopsisMovie) {
        this.sinopsisMovie = sinopsisMovie;
    }

    public String getVideoMovie() {
        return videoMovie;
    }

    public void setVideoMovie(String videoMovie) {
        this.videoMovie = videoMovie;
    }

    public String getGenderMovie() {
        return genderMovie;
    }

    public void setGenderMovie(String genderMovie) {
        this.genderMovie = genderMovie;
    }

    public String getCategoryMovie() {
        return categoryMovie;
    }

    public void setCategoryMovie(String categoryMovie) {
        this.categoryMovie = categoryMovie;
    }
}
