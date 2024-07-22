package com.example.meusflis.model;

public class User {
    public String email, password, name, bithyear, telephone, card;

    public User(String email, String password, String name, String bithyear, String telephone, String card) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.bithyear = bithyear;
        this.telephone = telephone;
        this.card = card;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBithyear() {
        return bithyear;
    }

    public void setBithyear(String bithyear) {
        this.bithyear = bithyear;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCard() {
        return card;
    }

    public void setCard(String card) {
        this.card = card;
    }
}
