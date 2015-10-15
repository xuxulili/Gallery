package com.data.gallery.gallery.model;

/**
 * Created by Administrator on 2015/9/16.
 */
public class PictureDetails {
    public String imageDetails;
    public String imageDetail_text;
    public String toString() {
        return imageDetail_text + imageDetails;
    }
    public void setImageDetails(String imageDetails) {
        this.imageDetails = imageDetails;
    }

    public void setImageDetail_text(String imageDetail_text) {
        this.imageDetail_text = imageDetail_text;
    }

    public String getImageDetails() {
        return imageDetails;
    }

    public String getImageDetail_text() {
        return imageDetail_text;
    }
}
