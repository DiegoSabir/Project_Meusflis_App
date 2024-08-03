package com.sabir.meusflis.Models;

public class FeatureModel {

    private String Fcast, Fcover, Fdes, Flink, Fthumb, Ftitle, Tlink;

    public FeatureModel() {
    }

    public FeatureModel(String fcast, String fcover, String fdes, String flink, String fthumb, String ftitle, String tlink) {
        Fcast = fcast;
        Fcover = fcover;
        Fdes = fdes;
        Flink = flink;
        Fthumb = fthumb;
        Ftitle = ftitle;
        Tlink = tlink;
    }

    public String getFcast() {
        return Fcast;
    }

    public void setFcast(String fcast) {
        Fcast = fcast;
    }

    public String getFcover() {
        return Fcover;
    }

    public void setFcover(String fcover) {
        Fcover = fcover;
    }

    public String getFdes() {
        return Fdes;
    }

    public void setFdes(String fdes) {
        Fdes = fdes;
    }

    public String getFlink() {
        return Flink;
    }

    public void setFlink(String flink) {
        Flink = flink;
    }

    public String getFthumb() {
        return Fthumb;
    }

    public void setFthumb(String fthumb) {
        Fthumb = fthumb;
    }

    public String getFtitle() {
        return Ftitle;
    }

    public void setFtitle(String ftitle) {
        Ftitle = ftitle;
    }

    public String getTlink() {
        return Tlink;
    }

    public void setTlink(String tlink) {
        Tlink = tlink;
    }
}
