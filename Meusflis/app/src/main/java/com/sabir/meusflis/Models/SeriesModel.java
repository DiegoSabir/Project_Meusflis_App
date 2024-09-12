package com.sabir.meusflis.Models;

import java.util.Map;

public class SeriesModel {

    private String background, cover, id, synopsis, title, trailer;
    private Map<String, String> cast;
    private Map<String, String> genre;
    private int likeCounter;
    private Map<String, EpisodeModel> episode;

    public SeriesModel() {
    }

    public SeriesModel(String background, String id, String cover, String synopsis, String title, String trailer, Map<String, String> cast, Map<String, String> genre, int likeCounter, Map<String, EpisodeModel> episode) {
        this.background = background;
        this.id = id;
        this.cover = cover;
        this.synopsis = synopsis;
        this.title = title;
        this.trailer = trailer;
        this.cast = cast;
        this.genre = genre;
        this.likeCounter = likeCounter;
        this.episode = episode;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<String, String> getCast() {
        return cast;
    }

    public void setCast(Map<String, String> cast) {
        this.cast = cast;
    }

    public Map<String, String> getGenre() {
        return genre;
    }

    public void setGenre(Map<String, String> genre) {
        this.genre = genre;
    }

    public Map<String, EpisodeModel> getEpisode() {
        return episode;
    }

    public void setEpisode(Map<String, EpisodeModel> episode) {
        this.episode = episode;
    }

    public int getLikeCounter() {
        return likeCounter;
    }

    public void setLikeCounter(int likeCounter) {
        this.likeCounter = likeCounter;
    }
}
