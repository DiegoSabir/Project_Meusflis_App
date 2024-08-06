package com.sabir.meusflis.Models;

public class EpisodeModel {

    String part, vidUrl;

    public EpisodeModel() {
    }

    public EpisodeModel(String part, String vidUrl) {
        this.part = part;
        this.vidUrl = vidUrl;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getVidUrl() {
        return vidUrl;
    }

    public void setVidUrl(String vidUrl) {
        this.vidUrl = vidUrl;
    }
}
