package com.nmbb.vlc.modle;

public class ListUrlData {
    private String values;
    private String outer_url;
    private String status;

    public ListUrlData(String valus, String outer_url, String status) {
        this.outer_url =outer_url;
        this.status = status;
        this.values = values;
    }

    public String getStatus() {
        return status;
    }

    public String getValues() {
        return values;
    }

    public String getOuter_url() {
        return outer_url;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public void setOuter_url(String outer_url) {
        this.outer_url = outer_url;
    }
}
