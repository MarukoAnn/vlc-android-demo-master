package com.nmbb.vlc.Util;

import com.nmbb.vlc.modle.ReturnStatusData;
import com.nmbb.vlc.modle.UpdatehttpData;
import com.google.gson.Gson;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by moonshine on 2018/4/27.
 */

public class SpostupdateHttp {

    public String posthttpresult(String Sresult,String path) {

        String SpostStatus = null;
        //okhttp Post请求传输Json数据
        OkHttpClient client = new OkHttpClient();
        UpdatehttpData Rdata = new UpdatehttpData();

        //将扫描的结果传输到实体
        Rdata.setSid(Sresult);
        Gson gson = new Gson();
        String json = gson.toJson(Rdata);//将其转换为JSON数据格式

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody requestBody = FormBody.create(JSON, json);//放进requestBoday中
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            //获取后台传输的额status状态码
            String result = response.body().string();


            //将接收到的JSON数据放到实体类里
            ReturnStatusData returnStatusData= gson.fromJson(result,ReturnStatusData.class);
            //定义一个参数来获取状态码
            SpostStatus = returnStatusData.getStatus();
        }catch (Exception e){
            e.printStackTrace();
        }
        return SpostStatus;
    }
}
