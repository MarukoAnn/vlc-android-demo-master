package com.nmbb.vlc.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.nmbb.vlc.R;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.UpdateHttp;
import com.nmbb.vlc.modle.SidSelectData;

import java.util.ArrayList;

/**
 * Created by moonshine on 2018/2/5.
 */

public class LaunchActivity extends Activity {
    String Msid=null;
    String updateApkUrl=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);
        UltimateBar.newColorBuilder()
                .statusColor(Color.parseColor("#13181c"))       // 状态栏颜色
                .statusDepth(50)                // 状态栏颜色深度
                .build(this)
                .apply();
        /**
         * 查询数据库里的sid
         */
        try {
            DataDBHepler dataDBHepler = new DataDBHepler(getBaseContext());
            if (dataDBHepler.isIdorSid()) {
                ArrayList<SidSelectData> DataList = dataDBHepler.FindSidData();
                final SidSelectData data = new SidSelectData(DataList.get(0).getId(), DataList.get(0).getSid(),DataList.get(0).getSysids());
                Msid = data.getSid();//获取数据库里的sid
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Integer time = 1100;
        Handler handler = new Handler();
        try {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        updatehttp();
                    }
                }, time);
        }catch (Exception e)
        {
                e.printStackTrace();
                updatehttp();
        }

        //加载启动图片

        //后台处理耗时任务

    }


    public void  updatehttp() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //耗时任务，比如加载网络数据
                    UpdateHttp updateHttp = new UpdateHttp();
                    try {
                        updateApkUrl = updateHttp.updatePostHttp();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"网络未连接", Toast.LENGTH_SHORT).show();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                if (Msid != null) {
                                    if (updateApkUrl == null) {
                                        Intent intent = new Intent();
                                        intent.putExtra("url", "null");
                                        //跳转至 MainActivity
                                        intent.setClass(LaunchActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        //结束当前的 Activity
                                        LaunchActivity.this.finish();
                                    } else {
                                        Intent intent = new Intent();
                                        intent.putExtra("url", updateApkUrl);
                                        //跳转至 MainActivity
                                        intent.setClass(LaunchActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        //结束当前的 Activity
                                        LaunchActivity.this.finish();
                                    }
                                } else {
                                    if (updateApkUrl == null) {
                                        Intent intent = new Intent();
                                        intent.putExtra("url", "null");
                                        //跳转至 MainActivity
                                        intent.setClass(LaunchActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        //结束当前的 Activity
                                        LaunchActivity.this.finish();
                                    } else {
                                        Intent intent = new Intent();
                                        intent.putExtra("url", updateApkUrl);
                                        //跳转至 MainActivity
                                        intent.setClass(LaunchActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        //结束当前的 Activity
                                        LaunchActivity.this.finish();
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Intent intent = new Intent();
                                intent.setClass(LaunchActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }).start();
        }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager connectivityManager =(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable()){
            Toast.makeText(getApplicationContext(),"网络未连接",Toast.LENGTH_SHORT).show();
        }
    }
}
