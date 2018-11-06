package com.nmbb.vlc.modle;

/**
 * Created by moonshine on 2018/1/27.
 */

public class ReturnStatusData {
    private String status;
    private String  sid;
    private String  systemInfo;

    public  ReturnStatusData(String sid,String systemInfo,String status) {
        this.sid = sid;
        this.systemInfo =systemInfo;
        this.status =status;
    }
    public String getStatus() {
        return status;
    }

    public String setStatus(String status) {
        this.status = status;
        return status;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSystemInfo() {
        return systemInfo;
    }

    public void setSystemInfo(String systemInfo) {
        this.systemInfo = systemInfo;
    }
}
