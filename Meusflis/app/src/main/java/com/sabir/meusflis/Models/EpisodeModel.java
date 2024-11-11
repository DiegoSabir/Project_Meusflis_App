package com.sabir.meusflis.Models;

public class EpisodeModel {

    String titleEpisode, imageEpisode, videoEpisode;

    public EpisodeModel() {
    }

    public EpisodeModel(String titleEpisode, String imageEpisode, String videoEpisode) {
        this.titleEpisode = titleEpisode;
        this.imageEpisode = imageEpisode;
        this.videoEpisode = videoEpisode;
    }

    public String getTitleEpisode() {
        return titleEpisode;
    }

    public void setTitleEpisode(String titleEpisode) {
        this.titleEpisode = titleEpisode;
    }

    public String getImageEpisode() {
        return imageEpisode;
    }

    public void setImageEpisode(String imageEpisode) {
        this.imageEpisode = imageEpisode;
    }

    public String getVideoEpisode() {
        return videoEpisode;
    }

    public void setVideoEpisode(String videoEpisode) {
        this.videoEpisode = videoEpisode;
    }
}
