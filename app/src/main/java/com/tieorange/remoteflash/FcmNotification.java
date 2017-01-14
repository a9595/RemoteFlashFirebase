package com.tieorange.remoteflash;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 1/14/17.
 */

public class FcmNotification {
    public FcmNotification(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }


    @SerializedName("data")
    @Expose
    public Notification notification;

    @SerializedName("to")
    @Expose
    public String to;

}