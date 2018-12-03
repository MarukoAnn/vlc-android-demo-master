package com.nmbb.vlc.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.zackratos.ultimatebar.UltimateBar;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.nmbb.vlc.R;
import com.nmbb.vlc.Util.CircleImageView;
import com.nmbb.vlc.Util.DataDBHepler;
import com.nmbb.vlc.Util.SavePamasInfo;
import com.nmbb.vlc.modle.RSpostData;
import com.nmbb.vlc.modle.ResultData;
import com.nmbb.vlc.modle.ReturnPostData;
import com.nmbb.vlc.modle.SidSelectData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

    private TextView       nameTv;
    private TextView       phoneTv;
    private TextView       idnumTv;
    private TextView       job_number;
    CircleImageView mCircleImageView;

    private PopupWindow pop;
    String        path;
    SavePamasInfo mSavePamasInfo = new SavePamasInfo();
    @SuppressLint({"WrongViewCast", "ResourceType"})
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        UltimateBar.newColorBuilder()
                .statusColor(Color.parseColor("#16abd9"))       // 状态栏颜色
                .statusDepth(50)                // 状态栏颜色深度
                .build(this)
                .apply();
        init();
        String path=mSavePamasInfo.getInfo(PersonalActivity.this,"imgPath","file");
        if (path.equals("")){
            mCircleImageView.setImageResource(R.drawable.ic_header);
        }else {
            Bitmap bm = BitmapFactory.decodeFile(path);
            mCircleImageView.setImageBitmap(bm);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                new AnotherTask().execute("");
            }
        }).start();
        ImageView imageView = findViewById(R.id.return_view);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("index", "1");
                intent.setClass(PersonalActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        mCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPop();
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

                nameTv.setText(returnPostData.getRealName());
                phoneTv.setText(returnPostData.getPhone());
                idnumTv.setText(returnPostData.getHomeTelephone());
                job_number.setText(returnPostData.getUserCode());

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
            String sid =selectSid();

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

    public  void init(){
        nameTv = findViewById(R.id.personal_name);
        phoneTv = findViewById(R.id.personal_phone);
        idnumTv = findViewById(R.id.personal_idnum);
        job_number = findViewById(R.id.job_number);
        mCircleImageView = findViewById(R.id.headerimg);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<LocalMedia> images;

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    images = PictureSelector.obtainMultipleResult(data);

                    LocalMedia media = images.get(0);
//                    path = media.getCutPath();
                    path = media.getPath();
//                    if (media.isCut() && !media.isCompressed()) {
//                        // 裁剪过
//                        path = media.getCutPath();
//                    }
                    Log.i("tag","路径："+path);
                    mSavePamasInfo.saveInfo(PersonalActivity.this,"imgPath",path,"file");
                    showImage(path);
            }
        }
    }
    //加载图片
    private void showImage(String imaePath){
        Bitmap bm = BitmapFactory.decodeFile(imaePath);
        mCircleImageView.setImageBitmap(bm);

    }

    private void showPop() {

        View bottomView = View.inflate(PersonalActivity.this, R.layout.layout_bottom_dialog, null);
        TextView mAlbum = (TextView) bottomView.findViewById(R.id.tv_album);
        TextView mCamera = (TextView) bottomView.findViewById(R.id.tv_camera);
        TextView mCancel = (TextView) bottomView.findViewById(R.id.tv_cancel);

        pop = new PopupWindow(bottomView, -1, -2);
        pop.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.5f;
        getWindow().setAttributes(lp);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        pop.setAnimationStyle(R.style.main_menu_photo_anim);
        pop.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.tv_album:
                        //相册
                        PictureSelector.create(PersonalActivity.this)
                                .openGallery(PictureMimeType.ofImage())
                                .selectionMode(PictureConfig.SINGLE)
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_camera:
                        //拍照
                        PictureSelector.create(PersonalActivity.this)
                                .openCamera(PictureMimeType.ofImage())
                                .forResult(PictureConfig.CHOOSE_REQUEST);
                        break;
                    case R.id.tv_cancel:
                        //取消
                        //closePopupWindow();
                        break;
                }
                closePopupWindow();
            }
        };

        mAlbum.setOnClickListener(clickListener);
        mCamera.setOnClickListener(clickListener);
        mCancel.setOnClickListener(clickListener);
    }

    public void closePopupWindow() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
            pop = null;
        }
    }
}

