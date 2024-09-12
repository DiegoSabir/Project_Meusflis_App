package com.sabir.meusflis.Models;

public class EpisodeModel {

    private String episode_id;
    private String episode_title;
    private String episode_url;

    public EpisodeModel() {
    }

    public EpisodeModel(String episode_id, String episode_title, String episode_url) {
        this.episode_id = episode_id;
        this.episode_title = episode_title;
        this.episode_url = episode_url;
    }

    public String getEpisodeId() {
        return episode_id;
    }

    public void setEpisodeId(String episode_id) {
        this.episode_id = episode_id;
    }

    public String getEpisodeTitle() {
        return episode_title;
    }

    public void setEpisodeTitle(String episode_title) {
        this.episode_title = episode_title;
    }

    public String getEpisodeUrl() {
        return episode_url;
    }

    public void setEpisodeUrl(String episode_url) {
        this.episode_url = episode_url;
    }
}
