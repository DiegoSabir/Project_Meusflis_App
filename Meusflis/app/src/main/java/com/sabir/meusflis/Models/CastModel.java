package com.sabir.meusflis.Models;

public class CastModel {

    private String name;
    private String url;

    public CastModel() {
    }

    public CastModel(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
