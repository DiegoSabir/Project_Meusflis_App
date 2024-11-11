package com.sabir.meusflis.Models;

public class SeriesModel {

    private String idSeries, cast, cover, synopsis, episodes, thumb, title, trailer;
    private int like;

    public SeriesModel() {
    }

    public SeriesModel(String idSeries, String cast, String cover, String synopsis, String episodes, String thumb, String title, String trailer, int like) {
        this.idSeries = idSeries;
        this.cast = cast;
        this.cover = cover;
        this.synopsis = synopsis;
        this.episodes = episodes;
        this.thumb = thumb;
        this.title = title;
        this.trailer = trailer;
        this.like = like;
    }

    public String getIdSeries() {
        return idSeries;
    }

    public void setIdSeries(String idSeries) {
        this.idSeries = idSeries;
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

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getEpisodes() {
        return episodes;
    }

    public void setEpisodes(String episodes) {
        this.episodes = episodes;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
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

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
