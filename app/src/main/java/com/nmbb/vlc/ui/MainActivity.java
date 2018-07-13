package com.nmbb.vlc.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nmbb.vlc.Fragment.FirstFragment;
import com.nmbb.vlc.Fragment.FourFragment;
import com.nmbb.vlc.Fragment.SecondFragment;
import com.nmbb.vlc.Fragment.ThirdFragment;
import com.nmbb.vlc.R;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.SpostupdateHttp;
import com.nmbb.vlc.modle.SidSelectData;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    // 初始化顶部栏显示
    private ImageView titleLeftImv;
    private TextView titleTv;
    // 定义4个Fragment对象
    private FirstFragment fg1;
    private SecondFragment fg2;
    private ThirdFragment fg3;
    private FourFragment fg4;
    // 帧布局对象，用来存放Fragment对象
    private FrameLayout frameLayout;
    // 定义每个选项中的相关控件
    private RelativeLayout firstLayout;
    private RelativeLayout secondLayout;
    private RelativeLayout thirdLayout;
    private RelativeLayout fourthLayout;
    private ImageView firstImage;
    private ImageView secondImage;
    private ImageView thirdImage;
    private ImageView fourthImage;
    private TextView firstText;
    private TextView secondText;
    private TextView thirdText;
    private TextView fourthText;
    // 定义几个颜色
    private int whirt = 0xFFFFFFFF;
    private int gray = 0xFF7597B3;
    private int dark = 0xff000000;

    String path = "http://120.78.137.182/element-admin/user/sid-update";
    // 定义FragmentManager对象管理器
    String index;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        initView(); // 初始化界面控件
        index = getIntent().getStringExtra("index");
        if (index!=null) {
            setChioceItem(Integer.parseInt(index));
        }
        else {
            setChioceItem(0); // 初始化页面加载时显示第一个选项卡
//            setUser();
        }

    }

    public void showNoProject(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage("身份验证已失效，请重新登录!")
                .setPositiveButton("确认", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this,LaunchActivity.class);
                        startActivity(intent);
                        dialog.dismiss();

                    }
                });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 初始化页面
     */
    private void initView() {
// 初始化页面标题栏
       titleLeftImv = (ImageView) findViewById(R.id.title_imv);
        titleTv = (TextView) findViewById(R.id.title_text_tv);
        titleTv.setText("首 页");
// 初始化底部导航栏的控件
        firstImage = (ImageView) findViewById(R.id.first_image);
        secondImage = (ImageView) findViewById(R.id.second_image);
        thirdImage = (ImageView) findViewById(R.id.third_image);
        fourthImage = (ImageView) findViewById(R.id.fourth_image);
        firstText = (TextView) findViewById(R.id.first_text);
        secondText = (TextView) findViewById(R.id.second_text);
        thirdText = (TextView) findViewById(R.id.third_text);
        fourthText = (TextView) findViewById(R.id.fourth_text);
        firstLayout = (RelativeLayout) findViewById(R.id.first_layout);
        secondLayout = (RelativeLayout) findViewById(R.id.second_layout);
        thirdLayout = (RelativeLayout) findViewById(R.id.third_layout);
        fourthLayout = (RelativeLayout) findViewById(R.id.fourth_layout);
        firstLayout.setOnClickListener(MainActivity.this);
        secondLayout.setOnClickListener(MainActivity.this);
        thirdLayout.setOnClickListener(MainActivity.this);
        fourthLayout.setOnClickListener(MainActivity.this);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    DataDBHepler dataDBHepler = new DataDBHepler(getBaseContext());
                    ArrayList<SidSelectData> DataList = dataDBHepler.FindSidData();
                    final SidSelectData data = new SidSelectData(DataList.get(0).getId(),DataList.get(0).getSid());
                    String Msid = data.getSid();//获取数据库里的sid

                    SpostupdateHttp spostupdateHttp = new SpostupdateHttp();
                    String result = spostupdateHttp.posthttpresult(Msid,path);
                    if (result.equals("13"))
                    {
                        showNoProject();
                        dataDBHepler = new DataDBHepler(getBaseContext());
                        dataDBHepler.delete("1");
                    }
                    else {
                        Log.i(TAG,"用户在线");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                Looper.loop();
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.first_layout:
                setChioceItem(0);
                break;
            case R.id.second_layout:
                setChioceItem(1);
                break;
            case R.id.third_layout:
                setChioceItem(2);
                break;
            case R.id.fourth_layout:
                setChioceItem(3);
                break;
            default:
                break;
        }
    }

    /**
     * 设置点击选项卡的事件处理
     *
     * @param index 选项卡的标号：0, 1, 2, 3
     */
    private void setChioceItem(int index) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        clearChioce(); // 清空, 重置选项, 隐藏所有Fragment
        hideFragments(fragmentTransaction);
        switch (index) {
            case 0:
// firstImage.setImageResource(R.drawable.XXXX); 需要的话自行修改
                firstText.setTextColor(dark);
                firstLayout.setBackgroundColor(gray);
// 如果fg1为空，则创建一个并添加到界面上
                if (fg1 == null) {
                    fg1 = new FirstFragment();
                    fragmentTransaction.add(R.id.content,fg1);
                } else {
// 如果不为空，则直接将它显示出来
                    fragmentTransaction.show(fg1);
                }
                break;
            case 1:
// secondImage.setImageResource(R.drawable.XXXX);
                secondText.setTextColor(dark);
                secondLayout.setBackgroundColor(gray);
                if (fg2 == null) {
                    fg2 = new SecondFragment();
                    fragmentTransaction.add(R.id.content, fg2);
                } else {
                    fragmentTransaction.show(fg2);
                }
                break;
            case 2:
// thirdImage.setImageResource(R.drawable.XXXX);
                thirdText.setTextColor(dark);
                thirdLayout.setBackgroundColor(gray);
                if (fg3 == null) {
                    fg3 = new ThirdFragment();
                    fragmentTransaction.add(R.id.content, fg3);
                } else {
                    fragmentTransaction.show(fg3);
                }
                break;
            case 3:
// fourthImage.setImageResource(R.drawable.XXXX);
                fourthText.setTextColor(dark);
                fourthLayout.setBackgroundColor(gray);
                if (fg4 == null) {
                    fg4 = new FourFragment();
                    fragmentTransaction.add(R.id.content, fg4);
                } else {
                    fragmentTransaction.show(fg4);
                }
                break;
        }
        fragmentTransaction.commit(); // 提交
    }

    /**
     * 当选中其中一个选项卡时，其他选项卡重置为默认
     */
    private void clearChioce() {
// firstImage.setImageResource(R.drawable.XXX);
        firstText.setTextColor(gray);
        firstLayout.setBackgroundColor(whirt);
// secondImage.setImageResource(R.drawable.XXX);
        secondText.setTextColor(gray);
        secondLayout.setBackgroundColor(whirt);
// thirdImage.setImageResource(R.drawable.XXX);
        thirdText.setTextColor(gray);
        thirdLayout.setBackgroundColor(whirt);
// fourthImage.setImageResource(R.drawable.XXX);
        fourthText.setTextColor(gray);
        fourthLayout.setBackgroundColor(whirt);
    }

    /**
     * 隐藏Fragment
     *
     * @param fragmentTransaction
     */
    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (fg1 != null) {
            fragmentTransaction.hide(fg1);
        }
        if (fg2 != null) {
            fragmentTransaction.hide(fg2);
        }
        if (fg3 != null) {
            fragmentTransaction.hide(fg3);
        }
        if (fg4 != null) {
            fragmentTransaction.hide(fg4);
        }
    }
}