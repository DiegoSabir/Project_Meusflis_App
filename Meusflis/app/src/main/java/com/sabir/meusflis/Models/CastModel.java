package com.sabir.meusflis.Models;

public class CastModel {

    String nameCast, imageCast;

    public CastModel() {
    }

    public CastModel(String nameCast, String imageCast) {
        this.nameCast = nameCast;
        this.imageCast = imageCast;
    }

    public String getNameCast() {
        return nameCast;
    }

    public void setNameCast(String nameCast) {
        this.nameCast = nameCast;
    }

    public String getImageCast() {
        return imageCast;
    }

    public void setImageCast(String imageCast) {
        this.imageCast = imageCast;
    }
}
