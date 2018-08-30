package com.nmbb.vlc.Util;

import com.nmbb.vlc.modle.UpdateData;
import com.nmbb.vlc.modle.UpdeateStatusData;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by moonshine on 2018/2/3.
 */

public class UpdateHttp {
   String status=null;
   String Updateurl=null;
    UpdeateStatusData updeateStatusData;
    public String updatePostHttp() {
        String path = "http://123.249.28.108:8081/element-admin/version/an-check";
//        String path = "http://192.168.43.65:8090/element-admin/version/an-check";
        String version = "1533965455521";
        UpdateData updateData = new UpdateData();
        updateData.setVersion(version);
        updateData.setType("APP05");
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(updateData);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = FormBody.create(JSON,json);
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        try {
            //获取后台返回的数据
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            //将数据装入实体类
            updeateStatusData = gson.fromJson(result,UpdeateStatusData.class);
            //获取返回的状态
            status=updeateStatusData.getStatus();
            Updateurl =updeateStatusData.getUrl();
        } catch (IOException e) {
            e.printStackTrace();
        }
       return Updateurl;
    }
}
