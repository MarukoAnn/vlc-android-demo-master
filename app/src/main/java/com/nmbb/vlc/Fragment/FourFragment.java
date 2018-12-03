package com.nmbb.vlc.Fragment;

/**
 * Created by User on 2017/11/29.
 */

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nmbb.vlc.R;
import com.nmbb.vlc.Util.CircleImageView;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.SavePamasInfo;
import com.nmbb.vlc.Util.SelecthttpUserUtil;
import com.nmbb.vlc.modle.RSpostData;
import com.nmbb.vlc.modle.ResultData;
import com.nmbb.vlc.modle.ReturnPostData;
import com.nmbb.vlc.modle.SidSelectData;
import com.nmbb.vlc.ui.LaunchActivity;
import com.nmbb.vlc.ui.PersonalActivity;
import com.nmbb.vlc.ui.Setpsw;
import com.nmbb.vlc.ui.banbenActivity;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

public class FourFragment extends Fragment {

    View            view;
    String          url = "http://119.23.219.22:80/element-admin/user/logout";
    SavePamasInfo   mSavePamasInfo = new SavePamasInfo();
    CircleImageView mCircleImageView;
    TextView tv_userCode;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg4, container, false);
        mCircleImageView =view.findViewById(R.id.h_head);
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
        tv_userCode = view.findViewById(R.id.userCode);
        new Thread(new Runnable() {
            @Override
            public void run() {
                new AnotherTask().execute("");
            }
        }).start();
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
    @Override
    public void onResume() {
        super.onResume();
        Log.i("tag","在线");
        String path = null;
        try {
            path = mSavePamasInfo.getInfo(getContext(),"imgPath","file");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (path.equals("")){

            mCircleImageView.setImageResource(R.drawable.ic_header);
        }else {
            Bitmap bm = BitmapFactory.decodeFile(path);
            mCircleImageView.setImageBitmap(bm);
        }
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
                tv_userCode.setText("工号："+returnPostData.getUserCode());

            }catch (Exception e){
                Log.e(TAG, "postlisthttp: ",e );
            }
        }
        @Override
        protected String doInBackground(String... params) {
            //耗时的操作
//            String  SidStatus = null;
            String result = null;
            String queryUrl = "http://119.23.219.22:80/element-admin/user/query-self";
            OkHttpClient client = new OkHttpClient();
            Gson gson = new Gson();

            ResultData mdata = new ResultData();
            String sid =SelectSid();

            mdata.setSid(sid);

            String json = gson.toJson(mdata);//将其转换为JSON数据格式

            MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

            RequestBody requestBody = RequestBody.create(mediaType, json);//放进requestBoday中
            Request request = new Request.Builder()
                    .url(queryUrl)
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
}