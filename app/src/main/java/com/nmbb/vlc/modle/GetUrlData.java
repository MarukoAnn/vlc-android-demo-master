package com.nmbb.vlc.modle;

import android.net.Uri;

import java.util.List;

public class GetUrlData {
    private String status;
    private String message;
    private UrlData values;



    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }



    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UrlData getValues() {
        return values;
    }

    public void setValues(UrlData values) {
        this.values = values;
    }
}
