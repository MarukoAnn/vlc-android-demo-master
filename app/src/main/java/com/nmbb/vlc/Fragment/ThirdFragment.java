package com.nmbb.vlc.Fragment;

/**
 * Created by User on 2017/11/29.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nmbb.vlc.modle.GetUrlData;
import com.nmbb.vlc.modle.ListUrlData;
import com.nmbb.vlc.modle.MyGridViewAdapter;
import com.nmbb.vlc.modle.MyViewPagerAdapter;
import com.nmbb.vlc.modle.ProductListBean;
import com.nmbb.vlc.modle.UrlData;
import com.nmbb.vlc.ui.MainActivity;
import com.nmbb.vlc.ui.VlcVideoActivity;
import com.nmbb.vlc.R;
import com.nmbb.vlc.modle.gridAdapter;

import java.io.Serializable;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static org.videolan.vlc.util.Preferences.TAG;


public class ThirdFragment extends Fragment implements ViewPager.OnPageChangeListener{
    @SuppressLint("StaticFieldLeak")
    static View view;
    gridAdapter adapter;
    List list;
    int index;
    String path = "http://120.78.137.182/element/QueryCameraAll";
    List<ListUrlData> listurl = new ArrayList<>();

    private ViewGroup points;//小圆点指示器
    private ImageView[] ivPoints;//小圆点图片集合
    private ViewPager viewPager;
    private int totalPage;//总的页数
    private int mPageSize = 8;//每页显示的最大数量
    private List<ProductListBean> listDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int currentPage;//当前页

    private String[] proName = {"名称0","名称1","名称2","名称3","名称4","名称5",
            "名称6","名称7","名称8","名称9","名称10","名称11","名称12","名称13",
            "名称14","名称15","名称16","名称17","名称18","名称19"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg3, container, false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                try {
                    HttpUrlMap(path);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(getActivity(),"网络错误",Toast.LENGTH_LONG).show();
                }

                Looper.loop();
            }
        }).start();
         init();

        iniViews();
        //模拟数据源
        setDatas();
        inflater = LayoutInflater.from(getContext());
        //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
        viewPagerList = new ArrayList<>();
        for(int i=0;i<totalPage;i++){
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.item_layout,viewPager,false);
            gridView.setAdapter(new MyGridViewAdapter(getContext(),listDatas,i,mPageSize));
            //添加item点击监听
            /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + currentPage*mPageSize;
                    Log.i("TAG","position的值为："+position + "-->pos的值为："+pos);
                    Toast.makeText(MainActivity.this,"你点击了 "+listDatas.get(pos).getProName(),Toast.LENGTH_SHORT).show();
                }
            });*/
            //每一个GridView作为一个View对象添加到ViewPager集合中
            viewPagerList.add(gridView);
        }
        //设置ViewPager适配器
        viewPager.setAdapter(new MyViewPagerAdapter(viewPagerList));
        //小圆点指示器
        ivPoints = new ImageView[totalPage];
        for(int i=0;i<ivPoints.length;i++){
            ImageView imageView = new ImageView(getContext());
            //设置图片的宽高
            imageView.setLayoutParams(new ViewGroup.LayoutParams(10,10));
            if(i == 0){
                imageView.setBackgroundResource(R.drawable.ic_xuanzhong);
            }else{
                imageView.setBackgroundResource(R.drawable.ic_chuxuan);
            }
            ivPoints[i] = imageView;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = 20;//设置点点点view的左边距
            layoutParams.rightMargin = 20;//设置点点点view的右边距
            points.addView(imageView,layoutParams);
        }
        //设置ViewPager滑动监听
        viewPager.addOnPageChangeListener(this);



//        grid();
        return view;
    }

    public void init() {
        final Spinner sp;
        sp = view.findViewById(R.id.TD_Spinner);
        final String[] list = {"生产现场", "厂外监控"};
        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
        final ArrayAdapter<String> listadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
        //为适配器设置下拉列表下拉时的菜单样式。
        listadapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        //将适配器添加到下拉列表上
        sp.setAdapter(listadapter);
        //为下拉列表控框定义点击，这个时间相应，菜单被点中
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                index = position;
                Log.i(TAG, "index的值为"+index);
                listadapter.notifyDataSetChanged();
//                grid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }






    private void iniViews() {
        viewPager = (ViewPager)view.findViewById(R.id.viewPager);
        //初始化小圆点指示器
        points = (ViewGroup)view.findViewById(R.id.points);
    }

    private void setDatas() {
        listDatas = new ArrayList<>();
        for(int i=0;i<proName.length;i++){
            listDatas.add(new ProductListBean(proName[i], R.drawable.img));
        }
    }

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    public void onPageSelected(int position) {
        //改变小圆圈指示器的切换效果
        setImageBackground(position);
        currentPage = position;
    }

    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 改变点点点的切换效果
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < ivPoints.length; i++) {
            if (i == selectItems) {
                ivPoints[i].setBackgroundResource(R.drawable.ic_xuanzhong);
            } else {
                ivPoints[i].setBackgroundResource(R.drawable.ic_chuxuan);
            }
        }
    }






//    public void grid() {
//        GridView gv = view.findViewById(R.id.GridView1);
//        //为GridView设置适配器
//        Log.i(TAG, "index的值为"+index);
//        adapter=new gridAdapter(getContext(),list,index);
//        gv.setAdapter(adapter);
//        //注册监听事件
//        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @SuppressLint("AuthLeak")
//            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                Intent intent =new Intent();
//                if (index==0)
//                {
//                    if (position==0)
//                    {
//                        intent.putExtra("Url", (Serializable) listurl.get(position));
//                    }
//                    else if (position==1)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/202?transportmode-unicast");
//                    }
//                    else if (position==2)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/302?transportmode-unicast");
//                    }
//                    else if (position==3)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/402?transportmode-unicast");
//                    }
//                    else if (position==4)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/502?transportmode-unicast");
//                    }
//                    else if (position==5)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/602?transportmode-unicast");
//                    }
//                    else if (position==6)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/702?transportmode-unicast");
//                    }
//                    else if (position==7)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/802?transportmode-unicast");
//                    }
//
//                }else if (index==1)
//                {
//                    if (position==0)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/902?transportmode-unicast");
//                    }
//                    else if (position==1)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1002?transportmode-unicast");
//                    }
//                    else if (position==2)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1102?transportmode-unicast");
//                    }
//                    else if (position==3)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1202?transportmode-unicast");
//                    }
//                    else if (position==4)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1302?transportmode-unicast");
//                    }
//                    else if (position==5)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1402?transportmode-unicast");
//                    }
//                    else if (position==6)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1502?transportmode-unicast");
//                    }
//                    else if (position==7)
//                    {
//                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1602?transportmode-unicast");
//                    }
//
//                }
//                intent.setClass(getActivity(),VlcVideoActivity.class);
//                startActivity(intent);
//            }
//        });
//    }
//    public class gridAdapter extends BaseAdapter {
//        //上下文对象
//        private Context context;
//        //图片数组
//        private Integer[] imgs;
//        private  int index ;
//        gridAdapter(Context context, List imgs, int index){
//            this.context = context;
//            this.index = index;
//            if (index==0)
//            {
//                this.imgs= new Integer[]{R.drawable.image, R.drawable.image,
//                        R.drawable.image, R.drawable.image,
//                        R.drawable.image, R.drawable.image,R.drawable.image,R.drawable.image,};
//            }
//            else {
//                this.imgs= new Integer[]{R.drawable.img, R.drawable.img,
//                        R.drawable.img, R.drawable.img,
//                        R.drawable.img, R.drawable.img,R.drawable.img,R.drawable.img,};
//            }
//        }
//        public int getCount() {
//            return imgs.length;
//        }
//
//        public Object getItem(int item) {
//            return item;
//        }
//
//        public long getItemId(int id) {
//            return id;
//        }
//
//        //创建View方法
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView imageView;
//            if (convertView == null) {
//                imageView = new ImageView(context);
//                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));//设置ImageView对象布局
//                imageView.setAdjustViewBounds(false);//设置边界对齐
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
//                imageView.setPadding(4, 4, 4, 4);//设置间距
//            }
//            else {
//                imageView = (ImageView) convertView;
//            }
//            imageView.setImageResource(imgs[position]);//为ImageView设置图片资源
//            return imageView;
//        }
//    }

    public  void HttpUrlMap(String path){

        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        MediaType JSON = MediaType.parse("application/json; charset = utf-8");
        FormBody body = new FormBody.Builder()
                .add("gid","1")
                .build();
        Request request = new Request.Builder()
                .url(path)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            String result = response.body().string();
            GetUrlData getUrlData = gson.fromJson(result,GetUrlData.class);
            Log.i(TAG,"最初为："+getUrlData.getValues());
            UrlData urlData = gson.fromJson(getUrlData.getValues().toString(),UrlData.class);
            Log.i(TAG,"数据为："+urlData.getData());
            ListUrlData listUrlData = gson.fromJson(urlData.getData().toString(),ListUrlData.class);
            List<ListUrlData> listdata = urlData.getData();
            for(int i=0;i<listurl.size();i++)
            {
                    String valus =listdata.get(i).getValues();
                    String status = listdata.get(i).getStatus();
                    String outer_url =listdata.get(i).getOuter_url();
                    ListUrlData p = new ListUrlData(valus,outer_url,status);
                    listurl.add(p);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}