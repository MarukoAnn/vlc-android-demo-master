package com.nmbb.vlc.modle;

/**
 * Created by moonshine on 2018/4/12.
 */

public class SelectOid {
    private String id;
    private String oid;

    public SelectOid(String id, String oid) {
        super();
        this.id = id;
        this.oid = oid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }
}
