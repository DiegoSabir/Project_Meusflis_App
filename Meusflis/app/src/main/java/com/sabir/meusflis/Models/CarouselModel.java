package com.sabir.meusflis.Models;

public class CarouselModel {

    private String Ttitle, Turl, Tvid;

    public CarouselModel() {
    }

    public CarouselModel(String ttitle, String turl, String tvid) {
        Ttitle = ttitle;
        Turl = turl;
        Tvid = tvid;
    }

    public String getTtitle() {
        return Ttitle;
    }

    public void setTtitle(String ttitle) {
        Ttitle = ttitle;
    }

    public String getTurl() {
        return Turl;
    }

    public void setTurl(String turl) {
        Turl = turl;
    }

    public String getTvid() {
        return Tvid;
    }

    public void setTvid(String tvid) {
        Tvid = tvid;
    }
}
