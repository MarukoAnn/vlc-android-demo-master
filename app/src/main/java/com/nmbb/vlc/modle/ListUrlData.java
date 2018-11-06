package com.nmbb.vlc.modle;

public class ListUrlData {
    private String value;
    private String outerUrl;
    private String status;

    public ListUrlData(String value, String outerUrl, String status) {
        this.outerUrl =outerUrl;
        this.status = status;
        this.value = value;
    }

    public String getStatus() {
        return status;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOuterUrl() {
        return outerUrl;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setOuterUrl(String outerUrl) {
        this.outerUrl = outerUrl;
    }
}
