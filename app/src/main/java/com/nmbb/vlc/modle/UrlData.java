package com.nmbb.vlc.modle;

import java.util.List;

public class UrlData {
    private String number;
    private List<ListUrlData> data;

    public List<ListUrlData> getData() {
        return data;
    }

    public String getNumber() {
        return number;
    }

    public void setData(List<ListUrlData> data) {
        this.data = data;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
