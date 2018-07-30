package com.nmbb.vlc.modle;

public class ListUrlData {
    private String value;
    private String outer_url;
    private String status;

    public ListUrlData(String value, String outer_url, String status) {
        this.outer_url =outer_url;
        this.status = status;
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOuter_url() {
        return outer_url;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setOuter_url(String outer_url) {
        this.outer_url = outer_url;
    }
}
