package com.nmbb.vlc.modle;

import android.content.Intent;

import java.util.List;

public class UrlData {
    private int number;
    private List<ListUrlData> datas;

    public List<ListUrlData> getDatas() {
        return datas;
    }


    public void setDatas(List<ListUrlData> datas) {
        this.datas = datas;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
