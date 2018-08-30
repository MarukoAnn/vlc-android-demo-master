package com.nmbb.vlc.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.nmbb.vlc.R;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.SelecthttpUserUtil;
import com.nmbb.vlc.modle.RSpostData;
import com.google.gson.Gson;
import com.nmbb.vlc.modle.ResultData;
import com.nmbb.vlc.modle.ReturnPostData;
import com.nmbb.vlc.modle.ReturnStatusData;
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
 * Created by moonshine on 2018/2/5.
 */

public class Setpsw extends Activity {

    private String id;
    private String userCode;
    private String idCode;
    private String realName;
    private String userName;
    private String homeAddress;
    private String homeTelephone;
    private String organizationId;
    private String password;
    private String phone;
    private String email;
    private String birthday;
    private String gender;
    private String idt;
    private String udt;
    private String newPsword;

    EditText user;
    EditText oldPsd;
    EditText newPsd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setpaw);
        intView();
        UltimateBar.newColorBuilder()
                .statusColor(Color.parseColor("#253847"))       // 状态栏颜色
                .statusDepth(50)                // 状态栏颜色深度
                .build(this)
                .apply();

        ImageView imageView = findViewById(R.id.returnview);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("index", "3");
                intent.setClass(Setpsw.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        /**
         * 查询数据库里的Sid
         */
        final DataDBHepler dataDBHepler = new DataDBHepler(getBaseContext());
        ArrayList<SidSelectData> DataList = dataDBHepler.FindSidData();
        final SidSelectData data = new SidSelectData(DataList.get(0).getId(),DataList.get(0).getSid(),DataList.get(0).getSysids());
        Log.i(TAG,"数据库的sid为："+data.getSid());
        final String Msid = data.getSid();


        Button btn = findViewById(R.id.setpsw_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                          try {
                              if(user.getText().toString().equals("") || oldPsd.getText().toString().equals("") ||newPsd.getText().toString().equals("")){

                                  Toast.makeText(getApplication(),"密码不能为空",Toast.LENGTH_SHORT).show();
                              }else if (!oldPsd.getText().toString().equals(newPsd.getText().toString())){
                                  Toast.makeText(getApplication(),"两次新密码输入不一致",Toast.LENGTH_SHORT).show();
                              }
                              else {
                                    final String RStus = postSidhttp(Msid,user.getText().toString(),newPsd.getText().toString());
                                    if (RStus.equals("10"))
                                    {
                                        String url = "http://123.249.28.108:8081/element-admin/user/logout";
                                        final SelecthttpUserUtil selecthttpUserUtil = new SelecthttpUserUtil();
                                        selecthttpUserUtil.postSidhttp(Msid,url);
                                    Toast.makeText(getApplicationContext(),"修改成功",Toast.LENGTH_SHORT).show();
                                    dataDBHepler.delete("1");
                                    Intent t = new Intent(Setpsw.this,LaunchActivity.class);
                                    startActivity(t);
                                    }
                                    else if(RStus.equals("20"))
                                    {
                                        Toast.makeText(getApplicationContext(),"用户名或密码错误输入错误",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(),"服务器故障",Toast.LENGTH_SHORT).show();
                                    }
                              }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }).start();
            }
        });
    }

    public void intView(){
        user = findViewById(R.id.set_oldpsw);
        oldPsd = findViewById(R.id.set_newpsw);
        newPsd = findViewById(R.id.set_sureSetpsw);
    }

    public String postSidhttp(String Sid, String oldpsword, String newpsword) {
        String  SidStatus = null;
        String  status = "20";
        newPsword = newpsword;
        String url = "http://123.249.28.108:8081/element-admin/user/query-self";
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        ResultData mdata = new ResultData();

        mdata.setSid(Sid);

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

            Log.i(TAG,"访问网络"+result);

            RSpostData rSpostData= gson.fromJson(result, RSpostData.class);
            Log.i(TAG,"data数据为："+rSpostData.getData());

            ReturnPostData returnPostData = rSpostData.getData();
            id =returnPostData.getId();
            userCode = returnPostData.getUserCode();
            idCode = returnPostData.getIdCode();
            realName = returnPostData.getRealName();
            userName = returnPostData.getUserName();
            homeAddress = returnPostData.getHomeAddress();
            homeTelephone = returnPostData.getHomeTelephone();
            organizationId = returnPostData.getOrganizationId();
            password = returnPostData.getPassword();
            phone = returnPostData.getPhone();
            email = returnPostData.getEmail();
            birthday = returnPostData.getBirthday();
            gender = returnPostData.getGender();
            idt = returnPostData.getIdt();
            udt = returnPostData.getUdt();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (oldpsword.equals(password)) {
            Log.i(TAG, "修改成功");
            SidStatus = ChangePsw();
            Log.i(TAG,"修改的密码返回值"+SidStatus);
            return SidStatus;
        } else {
            Toast.makeText(getApplicationContext(),"密码输入错误",Toast.LENGTH_SHORT).show();
            Log.i(TAG, "用户名或密码错误");
            return status;
        }
    }

    public String ChangePsw() {
        String mStatus =null;
        String path = "http://123.249.28.108:8081/element-admin/user/update-self";

        ReturnPostData data = new ReturnPostData(id,idCode,userCode,realName,userName,homeAddress,homeTelephone,organizationId,newPsword,phone,email,birthday,gender,idt,udt);

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();

        String json = gson.toJson(data);//将其转换为JSON数据格式

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(mediaType, json);//放进requestBoday中
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();

            ReturnStatusData returnStatusData= gson.fromJson(result,ReturnStatusData.class);
            mStatus = returnStatusData.getStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mStatus;
    }
}
