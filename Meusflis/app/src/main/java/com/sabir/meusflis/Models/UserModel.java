package com.sabir.meusflis.Models;

import java.util.List;

public class UserModel {

    private String id;
    private List<String> likeSeries;

    public UserModel() {}

    public UserModel(String id, List<String> likeSeries) {
        this.id = id;
        this.likeSeries = likeSeries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getLikeSeries() {
        return likeSeries;
    }

    public void setLikeSeries(List<String> likeSeries) {
        this.likeSeries = likeSeries;
    }
}
