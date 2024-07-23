package com.example.meusflis.Models;

public class EstrenosModel {

    String day, month, sinopsis, image;

    public EstrenosModel() {

    }

    public EstrenosModel(String day, String month, String sinopsis, String image) {
        this.day = day;
        this.month = month;
        this.sinopsis = sinopsis;
        this.image = image;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
