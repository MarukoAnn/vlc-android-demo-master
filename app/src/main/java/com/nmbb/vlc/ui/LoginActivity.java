package com.nmbb.vlc.ui;

/**
 * Created by User on 2017/11/27.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.DownloadUtil;
import com.nmbb.vlc.Util.UpdateDialog;
import com.nmbb.vlc.modle.LoginData;
import com.nmbb.vlc.modle.ReturnStatusData;
import com.nmbb.vlc.modle.UserpassData;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.nmbb.vlc.R;
import android.util.Log;
import android.os.Looper;

import java.io.IOException;

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

    String url;
    EditText username;
    EditText password;
    Button login_btn;
    String path = "http://120.78.137.182/element-admin/user/login";
    String Loginurl = "http://120.78.137.182/element-admin/user/logout";
    DataDBHepler dbHepler;
    String result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewLayout();
        init();
        dbHepler = new DataDBHepler(getBaseContext());
        try {
            if (dbHepler.isIdoruserpass()) {
                java.util.ArrayList<UserpassData> DataList1 = dbHepler.FinduserpassData();
                final UserpassData data1 = new UserpassData(DataList1.get(0).getId(), DataList1.get(0).getUser(), DataList1.get(0).getPassword());
                String Username = data1.getUser();
                String Password = data1.getPassword();
                username.setText(Username);
                password.setText(Password);
                username.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        username.setCursorVisible(true);
                    }
                });
                password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        password.setCursorVisible(true);
                    }
                });
            }
            else {
                Log.i(TAG,"没有用户名");

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        login_btn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                            result = GetPostLogin(username.getText().toString(), password.getText().toString(), path);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"网络无连接",Toast.LENGTH_SHORT).show();
                        }
                        try {
                            if (result.equals("10")) {
//                                String postsid = selectSid();
//                                if(postSidhttp(postsid)!=null) {

                                    if (dbHepler.isIdoruserpass())
                                    {
                                        dbHepler.updateUserpass(username.getText().toString(),password.getText().toString());
                                    }else {
                                        dbHepler.addUserpass("1",username.getText().toString(),password.getText().toString());
                                    }
//                                    if (dbHepler.isIdorUser()) {
//                                        dbHepler.updateUser(username.getText().toString(), realName, homeTelephone, birthday, phone);
//                                    } else {
//                                        dbHepler.addUser("1", username.getText().toString(), realName, homeTelephone, birthday, phone);
//                                    }
//                                }
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();//提示用户登录成功
                                Intent intent = new Intent();
                                intent.setClass(LoginActivity.this,MainActivity.class);
                                startActivity(intent);
                            } else if (result.equals("12")) {
                                Toast.makeText(getApplicationContext(), "登录失败，服务器故障", Toast.LENGTH_SHORT).show();//提示用户登录失败
                            } else if (result.equals("14")) {
                                Toast.makeText(getApplicationContext(), "登录失败，用户已存在不能重复登录", Toast.LENGTH_SHORT).show();//提示用户登录失败
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
    //控件初始化
    public void ViewLayout() {
        login_btn = findViewById(R.id.login_button);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
    }

    public void init() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        try{
            if (url.equals("null")) {
                Log.i(TAG, "已经为最新版本");

            } else {
                Log.i(TAG, "该下载链接为:" + url);
                showDialog();
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    public void showDialog() {
        downloadUtils =   new DownloadUtil(LoginActivity.this);
        final UpdateDialog Dialog = new UpdateDialog(LoginActivity.this);
        Dialog.setTitle("消息提示");
        Dialog.setMessage("发现新版本，是否更新?");
        Dialog.setYesOnclickListener("确定", new UpdateDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                downloadUtils.downloadAPK(url, "apk");
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

    public String GetPostLogin(String uname, String upsd,String path) {
        //okhttp Post请求传输Json数据
        OkHttpClient client = new OkHttpClient();
        LoginData Ldata = new LoginData();
        String postStatus = null;

        Ldata.setUname(uname);
        Ldata.setUpwd(upsd);
        Ldata.setModule("AN2");
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
            String postSid = resultStatusData.getSid();

            Log.i(TAG,"SID为"+postSid);
            if (dbHepler.isIdorSid()){
                dbHepler.update(postSid);
            }
            else {
                dbHepler.add("1",postSid);
            }
            postStatus= resultStatusData.getStatus();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return postStatus;
    }

}