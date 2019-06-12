package com.nmbb.vlc.ui;

/**
 * Created by User on 2017/11/27.
 */

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.DownloadUtil;
import com.nmbb.vlc.Util.UpdateDialog;
import com.nmbb.vlc.modle.LoginData;
import com.nmbb.vlc.modle.ReturnStatusData;
import com.nmbb.vlc.modle.SysInfoData;
import com.nmbb.vlc.modle.UserpassData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.nmbb.vlc.R;
import android.util.Log;
import android.os.Looper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.videolan.vlc.util.VLCInstance.TAG;


/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class LoginActivity extends Activity {
    private com.nmbb.vlc.Util.DownloadUtil downloadUtils;

    String updateApkUrl=null;
    EditText usernameEt;
    EditText passwordEt;
    Button loginBtn;
    String path = "http://119.23.219.22:80/element-admin/user/login";
//    String path = "http://106.13.108.160:8080/element-admin/user/login";
    DataDBHepler dbHepler;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //TODO 状态栏颜色

        UltimateBar.newColorBuilder()
                .statusColor(Color.parseColor("#253847"))       // 状态栏颜色
                .statusDepth(50)                // 状态栏颜色深度
                .build(this)
                .apply();
        ViewLayout();
        init();

        // TODO  数据库语柄初始化
        usernameEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameEt.setCursorVisible(true);
//                Log.i(TAG, "onClick: 点击");
            }
        });
        passwordEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordEt.setCursorVisible(true);
//                Log.i(TAG, "onClick: 点击");
            }
        });
        dbHepler = new DataDBHepler(getBaseContext());
        try {
            if (dbHepler.isIdoruserpass()) {
                java.util.ArrayList<UserpassData> DataList1 = dbHepler.FinduserpassData();
                final UserpassData data1 = new UserpassData(DataList1.get(0).getId(), DataList1.get(0).getUser(), DataList1.get(0).getPassword());
                String userName = data1.getUser();
                String passWord = data1.getPassword();
                // TODO 先查询是否已经拥有用户名密码
                usernameEt.setText(userName);
                passwordEt.setText(passWord);
                // TODO 点击时获取焦点

            }
            else {
                Log.i(TAG,"没有用户名");

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        loginBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO  获取网络连接
                ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()){
                        Toast.makeText(getApplicationContext(),"网络未连接",Toast.LENGTH_SHORT).show();
                }
               else {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Looper.prepare();
                        try {
                            // TODO 判断用户名密码是否为空
                            if(TextUtils.isEmpty(usernameEt.getText().toString().trim())||TextUtils.isEmpty(passwordEt.getText().toString().trim()))
                            {
                                Toast.makeText(LoginActivity.this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
                            }
                            result = GetPostLogin(usernameEt.getText().toString().trim(), passwordEt.getText().toString().trim(), path);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"网络错误或服务器故障",Toast.LENGTH_SHORT).show();
                        }
                        try {
                            if (result.equals("10")) {
//
                                    if (dbHepler.isIdoruserpass())
                                    {
                                        dbHepler.updateUserpass(usernameEt.getText().toString().trim(),passwordEt.getText().toString().trim());
                                    }else {
                                        dbHepler.addUserpass("1",usernameEt.getText().toString().trim(),passwordEt.getText().toString().trim());
                                    }
//
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();//提示用户登录成功
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            } else if (result.equals("12")) {
                                Toast.makeText(getApplicationContext(), "登录失败，服务器故障", Toast.LENGTH_SHORT).show();//提示用户登录失败
                            } else if (result.equals("14")) {
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();//提示用户登录成功
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "登录失败，用户名或密码错误", Toast.LENGTH_SHORT).show();//提示用户登录失败
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Looper.loop();
                    }
                }).start();
               }
            }
        });
    }
    //TODO 控件初始化
    public void ViewLayout() {
        loginBtn = findViewById(R.id.login_btn);
        usernameEt = findViewById(R.id.login_user);
        passwordEt = findViewById(R.id.login_pass);
    }

    //TODO 判断是否有更新
    public void init() {
        Intent intent = getIntent();
        updateApkUrl = intent.getStringExtra("url");
        try{
            if (updateApkUrl.equals("null")) {
                Log.i(TAG, "已经为最新版本");

            } else {
                Log.i(TAG, "该下载链接为:" + updateApkUrl);
                showDialog();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    // TODO  提示更新弹窗
    public void showDialog() {
        downloadUtils =   new DownloadUtil(LoginActivity.this);
        final UpdateDialog Dialog = new UpdateDialog(LoginActivity.this);
        Dialog.setTitle("消息提示");
        Dialog.setMessage("发现新版本，是否更新?");
        Dialog.setYesOnclickListener("确定", new UpdateDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                downloadUtils.downloadAPK(updateApkUrl, "apk");
                Dialog.dismiss();
            }
        });
        Dialog.setNoOnclickListener("取消", new UpdateDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                Dialog.dismiss();
            }
        });
        Dialog.show();
    }

    // TODO  登录请求
    public String GetPostLogin(String uname, String upsd,String path) {
        //okhttp Post请求传输Json数据
        OkHttpClient client = new OkHttpClient();
        LoginData Ldata = new LoginData();
        String postStatus = null;

        Ldata.setUname(uname);
        Ldata.setUpwd(upsd);
        Ldata.setModule("AN5");
        Gson gson = new Gson();
        String json = gson.toJson(Ldata);//将其转换为JSON数据格式

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        RequestBody requestBody = RequestBody.create(mediaType, json);//放进requestBoday中
        Request request = new Request.Builder()
                .url(path)
                .post(requestBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            ReturnStatusData resultStatusData= gson.fromJson(result,ReturnStatusData.class);
            String resultSid = resultStatusData.getSid();
            String resultSysInfo = resultStatusData.getSystemInfo();
            // TODO 解析数据
            List<SysInfoData> sysInfoData =gson.fromJson(resultSysInfo,new TypeToken<List<SysInfoData>>(){}.getType());
            Log.i(ContentValues.TAG,"SID为"+resultSid);
            Log.i(ContentValues.TAG,"SysId为"+resultSysInfo);
            Log.i(ContentValues.TAG,"列表为"+sysInfoData);
            List sysId =new ArrayList<>();
            try {
                for (int i=0;i<sysInfoData.size();i++)
                {
                    sysId.add(sysInfoData.get(i).getSysId());
                    Log.i("tag","sysid："+sysInfoData.get(i).getSysId());
                }
                if (dbHepler.isIdorSid()){
                    Log.i("tag","sysid:"+String.valueOf(sysId));
                    dbHepler.update(resultSid, String.valueOf(sysId));
                }
                else {
                    Log.i("tag","sysid:"+String.valueOf(sysId));
                    dbHepler.add("1",resultSid, String.valueOf(sysId));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            postStatus= resultStatusData.getStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postStatus;
    }

}