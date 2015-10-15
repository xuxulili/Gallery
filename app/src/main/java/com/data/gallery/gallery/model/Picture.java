package com.data.gallery.gallery.model;

/**
 * Created by Administrator on 2015/9/13.
 */
public class Picture {
    private String title;
    private String url;
    private String imgUrl;
    private String date;

    public String toString(){
        return title+url+"  "+imgUrl+"  "+date;
    }
    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
