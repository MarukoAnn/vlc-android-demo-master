package com.nmbb.vlc.modle;

/**
 * Created by moonshine on 2018/2/5.
 */

public class RSpostData {
        private ReturnPostData data;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setData(ReturnPostData data) {
        this.data = data;
    }

    public ReturnPostData getData() {
        return data;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

