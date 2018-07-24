package com.nmbb.vlc.Fragment;

/**
 * Created by User on 2017/11/29.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.nmbb.vlc.R;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.SelecthttpUserUtil;
import com.nmbb.vlc.modle.SidSelectData;
import com.nmbb.vlc.ui.LaunchActivity;
import com.nmbb.vlc.ui.PersonalActivity;
import com.nmbb.vlc.ui.Setpsw;
import com.nmbb.vlc.ui.banbenActivity;

import java.util.ArrayList;

public class FourFragment extends Fragment {

    View view;
    String url = "http://120.78.137.182/element-admin/user/logout";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg4, container, false);
    TextView tv =(TextView) view.findViewById(R.id.change_paw);
        tv.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(),Setpsw.class));
        }
    });
    TextView tv1=(TextView) view.findViewById(R.id.pensonal);
        tv1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(),PersonalActivity.class));
        }
    });
    TextView tv2 = view.findViewById(R.id.we);
        tv2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getActivity(),banbenActivity.class));
        }
    });
    /**
     *  登出
     */
    final SelecthttpUserUtil selecthttpUserUtil = new SelecthttpUserUtil();
    Button btn_out = (Button)view.findViewById(R.id.login_out);
        btn_out.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    Looper.prepare();
                    try {
                        String SID = SelectSid();
                        selecthttpUserUtil.postSidhttp(SID,url);
                        DataDBHepler dataDBHepler = new DataDBHepler(getContext());
                        dataDBHepler.delete("1");
                        startActivity(new Intent(getActivity(), LaunchActivity.class));
                        getActivity().finish();
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }

                    Looper.loop();
                }
            }).start();
        }

    });

        return view;
}

    /**
     * @param keyCode
     * @param event   监听手机back键 点击返回界面
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            //启动一个意图,回到桌面
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return view.onKeyDown(keyCode, event);
    }


    /**
     * 查询数据库里的sid
     */
    public String SelectSid(){

        DataDBHepler dataDBHepler = new DataDBHepler(getContext());
        ArrayList<SidSelectData> DataList = dataDBHepler.FindSidData();
        final SidSelectData data = new SidSelectData(DataList.get(0).getId(),DataList.get(0).getSid(),DataList.get(0).getSysids());

        final String Msid = data.getSid();//获取数据库里的sid
        return Msid;
    }
}