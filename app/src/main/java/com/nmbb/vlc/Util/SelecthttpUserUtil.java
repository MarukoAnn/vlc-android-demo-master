package com.nmbb.vlc.Util;

import android.util.Log;

import com.google.gson.Gson;
import com.nmbb.vlc.modle.ResultData;
import com.nmbb.vlc.modle.ReturnStatusData;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by moonshine on 2018/2/5.
 */

public class SelecthttpUserUtil {

    public String postSidhttp(String sid,String url) {

        String SidStatus = null;
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        ResultData mdata = new ResultData();

        mdata.setSid(sid);

        String json = gson.toJson(mdata);//将其转换为JSON数据格式

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(mediaType, json);//放进requestBoday中
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            Log.i(TAG, "访问网络" + result);

            ReturnStatusData data = gson.fromJson(result,ReturnStatusData.class);

            SidStatus = data.getStatus();

        } catch (IOException e) {
            e.printStackTrace();
        }
         Log.i(TAG, "定时更新的数据返回值为：" + SidStatus);
        return SidStatus;
    }
}
