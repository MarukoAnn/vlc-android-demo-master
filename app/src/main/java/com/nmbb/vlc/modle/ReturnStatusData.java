package com.nmbb.vlc.modle;

/**
 * Created by moonshine on 2018/1/27.
 */

public class ReturnStatusData {
    private String status;
    private String  sid;
    private String  sysids;

    public String getStatus() {
        return status;
    }

    public String setStatus(String status) {
        this.status = status;
        return status;
    }

    public String getSysids() {
        return sysids;
    }

    public void setSysids(String sysids) {
        this.sysids = sysids;
    }

    public String getSid() {
        return sid;
    }

    public String setSid(String sid,String sysids) {
        this.sid = sid;
        this.sysids =sysids;
        return sid;
    }
}
