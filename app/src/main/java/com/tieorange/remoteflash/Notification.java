package com.tieorange.remoteflash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 1/14/17.
 */

public class Notification {
    public Notification() {
    }

    public Notification(String body, String title) {
        this.body = body;
        this.title = title;
    }

    @SerializedName("body")
    @Expose
    public String body;
    @SerializedName("title")
    @Expose
    public String title;

    /*@SerializedName("icon")
    @Expose
    public String icon;*/
}
