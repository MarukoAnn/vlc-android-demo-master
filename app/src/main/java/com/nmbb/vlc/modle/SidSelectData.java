package com.nmbb.vlc.modle;

/**
 * Created by moonshine on 2018/2/5.
 */

public class SidSelectData {
    private String id;
    private String sid;
    private String sysids;


    public SidSelectData(String id, String sid,String sysids) {
        super();
        this.id = id;
        this.sid = sid;
        this.sysids = sysids;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSysids() {
        return sysids;
    }

    public void setSysids(String sysids) {
        this.sysids = sysids;
    }
}
