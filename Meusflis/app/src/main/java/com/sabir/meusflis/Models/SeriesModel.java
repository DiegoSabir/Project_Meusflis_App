package com.sabir.meusflis.Models;

public class SeriesModel {

    String background, cast, cover, episodes, synopsis, title, trailer;

    public SeriesModel() {
    }

    public SeriesModel(String background, String cast, String cover, String episodes, String synopsis, String title, String trailer) {
        this.background = background;
        this.cast = cast;
        this.cover = cover;
        this.episodes = episodes;
        this.synopsis = synopsis;
        this.title = title;
        this.trailer = trailer;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }
}
