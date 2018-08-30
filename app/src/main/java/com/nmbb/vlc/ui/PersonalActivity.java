package com.nmbb.vlc.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.nmbb.vlc.R;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.modle.RSpostData;
import com.nmbb.vlc.modle.ResultData;
import com.nmbb.vlc.modle.ReturnPostData;
import com.nmbb.vlc.modle.SidSelectData;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by moonshine on 2018/3/17.
 */

public class PersonalActivity extends Activity {


    private String realName;
    private String homeTelephone;
    private String phone;
    private String birthday;

    private TextView name;
    private TextView p_phone;
    private TextView idnum;
    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        UltimateBar.newColorBuilder()
                .statusColor(Color.parseColor("#253847"))       // 状态栏颜色
                .statusDepth(50)                // 状态栏颜色深度
                .build(this)
                .apply();
        init();
        ImageView imageView = findViewById(R.id.return_view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new AnotherTask().execute("");
            }
        }).start();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("index", "3");
                intent.setClass(PersonalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

        /**
         *获取Sid数据
         */

    public String  selectSid(){
        DataDBHepler dataDBHepler = new DataDBHepler(getBaseContext());
        ArrayList<SidSelectData> DataList = dataDBHepler.FindSidData();
        final SidSelectData data = new SidSelectData(DataList.get(0).getId(),DataList.get(0).getSid(),DataList.get(0).getSysids());
        Log.i(TAG,"数据库的sid为："+data.getSid());
        final String Msid = data.getSid();
        return Msid;
    }

        private class AnotherTask extends AsyncTask<String, Void, String> {
            @Override
            protected void onPostExecute(String result) {
                //对UI组件的更新操作
                Gson gson = new Gson();
                try {
                    RSpostData rSpostData= gson.fromJson(result, RSpostData.class);
                    Log.i(TAG,"data数据为："+rSpostData.getData());

                    ReturnPostData returnPostData = rSpostData.getData();

                    name.setText(returnPostData.getRealName());
                    p_phone.setText(returnPostData.getPhone());
                    idnum.setText(returnPostData.getIdCode());

                }catch (Exception e){
                    Log.e(TAG, "postlisthttp: ",e );
                }
            }
            @Override
            protected String doInBackground(String... params) {
                //耗时的操作
                String  SidStatus = null;
                String result = null;
                String url = "http://123.249.28.108:8081/element-admin/user/query-self";
                OkHttpClient client = new OkHttpClient();
                Gson gson = new Gson();

                ResultData mdata = new ResultData();
                String sid =selectSid();

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
                    result = response.body().string();

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "doInBackground: ",e );
                }
                return result;
            }
        }

    public  void init(){
        name = findViewById(R.id.personal_name);
        p_phone = findViewById(R.id.personal_phone);
        idnum = findViewById(R.id.personal_idnum);
    }
}
