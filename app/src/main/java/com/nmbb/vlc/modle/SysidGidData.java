package com.nmbb.vlc.modle;

import java.util.List;

public class SysidGidData {
    private String status;
    private List<DataGid> datas;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<DataGid> getDatas() {
        return datas;
    }

    public void setDatas(List<DataGid> datas) {
        this.datas = datas;
    }
}
