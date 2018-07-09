package com.nmbb.vlc.Fragment;

/**
 * Created by User on 2017/11/29.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.nmbb.vlc.ui.MainActivity;
import com.nmbb.vlc.ui.VlcVideoActivity;
import com.nmbb.vlc.R;
import com.nmbb.vlc.modle.gridAdapter;

import java.util.List;

import static org.videolan.vlc.util.Preferences.TAG;


public class ThirdFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    static View view;
    gridAdapter adapter;
    List list;
    int index;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg3, container, false);
        init();
        grid();
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
                grid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void grid() {
        GridView gv = view.findViewById(R.id.GridView1);
        //为GridView设置适配器
        Log.i(TAG, "index的值为"+index);
        adapter=new gridAdapter(getContext(),list,index);
        gv.setAdapter(adapter);
        //注册监听事件
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressLint("AuthLeak")
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent =new Intent();
                if (index==0)
                {
                    if (position==0)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/102?transportmode-unicast");
                    }
                    else if (position==1)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/202?transportmode-unicast");
                    }
                    else if (position==2)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/302?transportmode-unicast");
                    }
                    else if (position==3)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/402?transportmode-unicast");
                    }
                    else if (position==4)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/502?transportmode-unicast");
                    }
                    else if (position==5)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/602?transportmode-unicast");
                    }
                    else if (position==6)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/702?transportmode-unicast");
                    }
                    else if (position==7)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/802?transportmode-unicast");
                    }

                }else if (index==1)
                {
                    if (position==0)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/902?transportmode-unicast");
                    }
                    else if (position==1)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1002?transportmode-unicast");
                    }
                    else if (position==2)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1102?transportmode-unicast");
                    }
                    else if (position==3)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1202?transportmode-unicast");
                    }
                    else if (position==4)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1302?transportmode-unicast");
                    }
                    else if (position==5)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1402?transportmode-unicast");
                    }
                    else if (position==6)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1502?transportmode-unicast");
                    }
                    else if (position==7)
                    {
                        intent.putExtra("Url", "rtsp://admin:12345678a@222.85.147.216:554/Streaming/Channels/1602?transportmode-unicast");
                    }

                }
                intent.setClass(getActivity(),VlcVideoActivity.class);
                startActivity(intent);
            }
        });
    }
    public class gridAdapter extends BaseAdapter {
        //上下文对象
        private Context context;
        //图片数组
        private Integer[] imgs;
        private  int index ;
        gridAdapter(Context context, List imgs, int index){
            this.context = context;
            this.index = index;
            if (index==0)
            {
                this.imgs= new Integer[]{R.drawable.image, R.drawable.image,
                        R.drawable.image, R.drawable.image,
                        R.drawable.image, R.drawable.image,R.drawable.image,R.drawable.image,};
            }
            else {
                this.imgs= new Integer[]{R.drawable.img, R.drawable.img,
                        R.drawable.img, R.drawable.img,
                        R.drawable.img, R.drawable.img,R.drawable.img,R.drawable.img,};
            }
        }
        public int getCount() {
            return imgs.length;
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 300));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(4, 4, 4, 4);//设置间距
            }
            else {
                imageView = (ImageView) convertView;
            }
            imageView.setImageResource(imgs[position]);//为ImageView设置图片资源
            return imageView;
        }
    }

}