package com.example.meusflis.model;

public class Episode {
    int episodeNumber;
    String episodeTitle;

    public Episode(int episodeNumber, String episodeTitle) {
        this.episodeNumber = episodeNumber;
        this.episodeTitle = episodeTitle;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public void setEpisodeTitle(String episodeTitle) {
        this.episodeTitle = episodeTitle;
    }
}
