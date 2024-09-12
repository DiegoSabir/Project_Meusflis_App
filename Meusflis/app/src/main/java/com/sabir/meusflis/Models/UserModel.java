package com.sabir.meusflis.Models;

import java.util.Map;

public class UserModel {

    private String id, name, surname, email, address, telephone, mobile;
    private Map<String, String> likeSeries;

    public UserModel() {
    }

    public UserModel(String id, String name, String surname, String email, String telephone, String address, String mobile, Map<String, String> likeSeries) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.telephone = telephone;
        this.address = address;
        this.mobile = mobile;
        this.likeSeries = likeSeries;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Map<String, String> getLikeSeries() {
        return likeSeries;
    }

    public void setLikeSeries(Map<String, String> likeSeries) {
        this.likeSeries = likeSeries;
    }
}
