package com.nmbb.vlc.Fragment;

/**
 * Created by User on 2017/11/29.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.nmbb.vlc.modle.ListUrlData;
import com.nmbb.vlc.modle.ProductListBean;
import com.nmbb.vlc.ui.MainActivity;
import com.nmbb.vlc.ui.VlcVideoActivity;
import com.nmbb.vlc.R;

import java.util.ArrayList;
import java.util.List;

import static org.videolan.vlc.util.Preferences.TAG;


public class ThirdFragment extends Fragment {
    @SuppressLint("StaticFieldLeak")
    static View view;
    List list;
    int index;
    GridView gv;
    private ViewGroup points;//小圆点指示器
    private ImageView[] ivPoints;//小圆点图片集合
    //    private ViewPager viewPager;
    private int totalPage;//总的页数
    private int mPageSize = 9;//每页显示的最大数量
    private List<ProductListBean> listDatas;//总的数据源
    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
    private int currentPage;//当前页
    private String[] prourl;//提取MainActivity里面传递来的链接数据
    private String[] proName;
    private Integer[] imgs;
    MyGridViewAdapter myGridViewAdapter;
//    MyViewPagerAdapter myViewPagerAdapter;
    Spinner sp;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        int PasnameLength = ((MainActivity) activity).getPasnameLength();
        Log.i(TAG, "长度为" + PasnameLength);
//        int PasurlLength  = ((MainActivity)activity).getPasurlLength();
        proName = new String[PasnameLength];
        prourl = new String[PasnameLength];
        proName = ((MainActivity) activity).getpasname();
        prourl = ((MainActivity) activity).getPasurl();//通过强转成宿主activity，就可以获取到传递过来的数据
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fg3, container, false);
        setDatas();
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
                Log.i(TAG, "位置" + position);
                index = position;
                setDatas();
                listadapter.notifyDataSetChanged();
                grid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setDatas() {
        setimage(index);
        listDatas = new ArrayList<>();
        try {
            for (int i = 0; i < proName.length; i++) {
                listDatas.add(new ProductListBean(proName[i], imgs[i]));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setimage(int indexq) {
        if (indexq == 0) {
            imgs = new Integer[]{R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4,
                    R.drawable.image5, R.drawable.image6, R.drawable.image7, R.drawable.image, R.drawable.image,
                    R.drawable.image7, R.drawable.image2, R.drawable.image, R.drawable.image, R.drawable.image,
                    R.drawable.image3, R.drawable.image1, R.drawable.image4, R.drawable.image5, R.drawable.image,
                    R.drawable.image, R.drawable.image1, R.drawable.image, R.drawable.image};
        } else {
            imgs = new Integer[]{R.drawable.image7, R.drawable.image6, R.drawable.image4, R.drawable.image5,
                    R.drawable.image3, R.drawable.image2, R.drawable.image1, R.drawable.image5, R.drawable.image4,
                    R.drawable.image6, R.drawable.image1, R.drawable.image3, R.drawable.image2, R.drawable.image1,
                    R.drawable.image5, R.drawable.image6, R.drawable.image, R.drawable.image, R.drawable.image,
                    R.drawable.image, R.drawable.image5, R.drawable.image4, R.drawable.image3};
            Log.i(TAG, "图片二" + imgs);
        }
    }

    public void grid() {
        GridView gv = view.findViewById(R.id.gridView1);
        //为GridView设置适配器
       myGridViewAdapter = new MyGridViewAdapter(getContext(), listDatas);
       gv.setAdapter(myGridViewAdapter);
        //注册监听事件
    }




    public class MyGridViewAdapter extends BaseAdapter {

        private List<ProductListBean> listData;
        private LayoutInflater inflater;
        private Context context;


        public MyGridViewAdapter(Context context, List<ProductListBean> listData) {
            this.context = context;
            this.listData = listData;

            inflater = LayoutInflater.from(context);
        }

        /**
         * 先判断数据集的大小是否足够显示满本页？listData.size() > (mIndex + 1)*mPagerSize
         * 如果满足，则此页就显示最大数量mPagerSize的个数
         * 如果不够显示每页的最大数量，那么剩下几个就显示几个 (listData.size() - mIndex*mPagerSize)
         */
        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position ;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_gridview, parent, false);
                holder = new ViewHolder();
                holder.proName = (TextView) convertView.findViewById(R.id.proName);
                holder.imgUrl = (ImageView) convertView.findViewById(R.id.imgUrl);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //重新确定position（因为拿到的是总的数据源，数据源是分页加载到每页的GridView上的，为了确保能正确的点对不同页上的item）
            final int pos = position;//假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
            final ProductListBean bean = listData.get(pos);
//            final ListUrlData data = bean.getListurl();
            holder.proName.setText(bean.getProName());
            holder.imgUrl.setImageResource(bean.getImgUrl());

            //添加item监听
            convertView.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("AuthLeak")
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("Url",prourl[position]);
                    intent.setClass(getActivity(), VlcVideoActivity.class);
                    startActivity(intent);
                    Log.i(TAG,"链接为："+prourl[position]);
                    Toast.makeText(context, "你点击了 " + bean.getProName(), Toast.LENGTH_SHORT).show();
                }
            });
            return convertView;
        }

        class ViewHolder {
            private TextView proName;
            private ImageView imgUrl;
        }
    }


}







//package com.nmbb.vlc.Fragment;
//
///**
// * Created by User on 2017/11/29.
// */
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Looper;
//import android.support.v4.app.Fragment;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.nmbb.vlc.modle.ListUrlData;
//import com.nmbb.vlc.modle.MyViewPagerAdapter;
//import com.nmbb.vlc.modle.ProductListBean;
//import com.nmbb.vlc.ui.MainActivity;
//import com.nmbb.vlc.ui.VlcVideoActivity;
//import com.nmbb.vlc.R;
//import com.nmbb.vlc.modle.gridAdapter;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
//import static org.videolan.vlc.util.Preferences.TAG;
//
////implements ViewPager.OnPageChangeListener
//
//public class ThirdFragment extends Fragment{
//    @SuppressLint("StaticFieldLeak")
//    static View view;
//    List list;
//    int index;
//    String path = "http://120.78.137.182/element/QueryCameraAll";
//    List<ListUrlData> listurl = new ArrayList<>();
//
//    private ViewGroup points;//小圆点指示器
//    private ImageView[] ivPoints;//小圆点图片集合
//    private ViewPager viewPager;
//    private int totalPage;//总的页数
//    private int mPageSize = 9;//每页显示的最大数量
//    private List<ProductListBean> listDatas;//总的数据源
//    private List<View> viewPagerList;//GridView作为一个View对象添加到ViewPager集合中
//    private int currentPage;//当前页
//    private String[] prourl;//提取MainActivity里面传递来的链接数据
//    private String[] proName;
//    private Integer[] imgs;
//    MyGridViewAdapter myGridViewAdapter;
//    MyViewPagerAdapter myViewPagerAdapter;
//    Spinner sp;
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        int PasnameLength = ((MainActivity)activity).getPasnameLength();
//        Log.i(TAG,"长度为"+PasnameLength);
////        int PasurlLength  = ((MainActivity)activity).getPasurlLength();
//        proName = new String[PasnameLength];
//        prourl = new String[PasnameLength];
//        proName = ((MainActivity) activity).getpasname();
//        prourl = ((MainActivity)activity).getPasurl();//通过强转成宿主activity，就可以获取到传递过来的数据
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fg3, container, false);
//        iniViews();
//        //模拟数据源
//        setDatas();
//
//
////        inflater = LayoutInflater.from(getContext());
////        //总的页数，取整（这里有三种类型：Math.ceil(3.5)=4:向上取整，只要有小数都+1  Math.floor(3.5)=3：向下取整  Math.round(3.5)=4:四舍五入）
////        totalPage = (int) Math.ceil(listDatas.size() * 1.0 / mPageSize);
////        viewPagerList = new ArrayList<>();
////        for (int i = 0; i < totalPage; i++) {
////            //每个页面都是inflate出一个新实例
////            GridView gridView = (GridView) inflater.inflate(R.layout.item_layout, viewPager, false);
////            myGridViewAdapter = new MyGridViewAdapter(getContext(), listDatas, i, mPageSize);
////            gridView.setAdapter(myGridViewAdapter);
////            //添加item点击监听
////            /*gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
////                @Override
////                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    int pos = position + currentPage*mPageSize;
////                    Log.i("TAG","position的值为："+position + "-->pos的值为："+pos);
////                    Toast.makeText(MainActivity.this,"你点击了 "+listDatas.get(pos).getProName(),Toast.LENGTH_SHORT).show();
////                }
////            });*/
////            //每一个GridView作为一个View对象添加到ViewPager集合中
////            viewPagerList.add(gridView);
////        }
////        //设置ViewPager适配器
////        myViewPagerAdapter = new MyViewPagerAdapter(viewPagerList);
////        viewPager.setAdapter(myViewPagerAdapter);
////        //小圆点指示器
////        ivPoints = new ImageView[totalPage];
////        for (int i = 0; i < ivPoints.length; i++) {
////            ImageView imageView = new ImageView(getContext());
////            //设置图片的宽高
////            imageView.setLayoutParams(new ViewGroup.LayoutParams(10, 10));
////            if (i == 0) {
////                imageView.setBackgroundResource(R.drawable.ic_xuanzhong);
////            } else {
////                imageView.setBackgroundResource(R.drawable.ic_chuxuan);
////            }
////            ivPoints[i] = imageView;
////            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
////            layoutParams.leftMargin = 20;//设置点点点view的左边距
////            layoutParams.rightMargin = 20;//设置点点点view的右边距
////            points.addView(imageView, layoutParams);
////        }
////        //设置ViewPager滑动监听
////        viewPager.addOnPageChangeListener(this);
//
////        grid();
//
//
//        /**
//         * 设置spinner的下拉刷新gridview
//         */
//        sp = view.findViewById(R.id.TD_Spinner);
//        final String[] list = {"生产现场", "厂外监控"};
//        //第二步：为下拉列表定义一个适配器，这里就用到里前面定义的list。
//        final ArrayAdapter<String> listadapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
//        //为适配器设置下拉列表下拉时的菜单样式。
//        listadapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
//        //将适配器添加到下拉列表上
//        sp.setAdapter(listadapter);
//        //为下拉列表控框定义点击，这个时间相应，菜单被点中
//        final LayoutInflater finalInflater = inflater;
//        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                index = position;
//                viewPagerList.clear();
////                setDatas();
////                for (int i = 0; i < totalPage; i++) {
////                    //每个页面都是inflate出一个新实例
////                    GridView gridView = (GridView) finalInflater.inflate(R.layout.item_layout, viewPager, false);
////                    myGridViewAdapter = new MyGridViewAdapter(getContext(), listDatas, i, mPageSize);
////                    gridView.setAdapter(myGridViewAdapter);
////                    //每一个GridView作为一个View对象添加到ViewPager集合中
////                    viewPagerList.add(gridView);
////                }
////                //设置ViewPager适配器
////                myViewPagerAdapter = new MyViewPagerAdapter(viewPagerList);
////                viewPager.setAdapter(myViewPagerAdapter);
////                myViewPagerAdapter.notifyDataSetChanged();
////                listadapter.notifyDataSetChanged();
//            }
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//
//        return view;
//    }
//
////    private void iniViews() {
////        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
////        //初始化小圆点指示器
////        points = (ViewGroup) view.findViewById(R.id.points);
////
////    }
//

//}

//    }
//
////    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
////
////    }
//////
//////    public void onPageSelected(int position) {
//////        //改变小圆圈指示器的切换效果
//////        setImageBackground(position);
//////        currentPage = position;
//////    }
////
////    public void onPageScrollStateChanged(int state) {
////
////    }
//
//    /**
//     * 改变点点点的切换效果
//     *
////     * @param selectItems
////     */
////    private void setImageBackground(int selectItems) {
////        for (int i = 0; i < ivPoints.length; i++) {
////            if (i == selectItems) {
////                ivPoints[i].setBackgroundResource(R.drawable.ic_xuanzhong);
////            } else {
////                ivPoints[i].setBackgroundResource(R.drawable.ic_chuxuan);
////            }
////        }
////    }
//
//

//    public class MyGridViewAdapter extends BaseAdapter {
//
//        private List<ProductListBean> listData;
//        private LayoutInflater inflater;
//        private Context context;
//        private int mIndex;//页数下标，表示第几页，从0开始
//        private int mPagerSize;//每页显示的最大数量
//
//
//        public MyGridViewAdapter(Context context, List<ProductListBean> listData, int mIndex, int mPagerSize) {
//            this.context = context;
//            this.listData = listData;
//            this.mIndex = mIndex;
//            this.mPagerSize = mPagerSize;
//            inflater = LayoutInflater.from(context);
//        }
//
//        /**
//         * 先判断数据集的大小是否足够显示满本页？listData.size() > (mIndex + 1)*mPagerSize
//         * 如果满足，则此页就显示最大数量mPagerSize的个数
//         * 如果不够显示每页的最大数量，那么剩下几个就显示几个 (listData.size() - mIndex*mPagerSize)
//         */
//        @Override
//        public int getCount() {
//            return listData.size() > (mIndex + 1) * mPagerSize ? mPagerSize : (listData.size() - mIndex * mPagerSize);
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return listData.get(position + mIndex * mPagerSize);
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position + mIndex * mPagerSize;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            ViewHolder holder = null;
//            if (convertView == null) {
//                convertView = inflater.inflate(R.layout.item_gridview, parent, false);
//                holder = new ViewHolder();
//                holder.proName = (TextView) convertView.findViewById(R.id.proName);
//                holder.imgUrl = (ImageView) convertView.findViewById(R.id.imgUrl);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            //重新确定position（因为拿到的是总的数据源，数据源是分页加载到每页的GridView上的，为了确保能正确的点对不同页上的item）
//            final int pos = position + mIndex * mPagerSize;//假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
//            final ProductListBean bean = listData.get(pos);
////            final ListUrlData data = bean.getListurl();
//            holder.proName.setText(bean.getProName());
//            holder.imgUrl.setImageResource(bean.getImgUrl());
//
//            //添加item监听
//            convertView.setOnClickListener(new View.OnClickListener() {
//                @SuppressLint("AuthLeak")
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent();
//                    intent.putExtra("Url",prourl[position]);
//                    intent.setClass(getActivity(), VlcVideoActivity.class);
//                    startActivity(intent);
//                    Log.i(TAG,"链接为："+prourl[position]);
//                    Toast.makeText(context, "你点击了 " + bean.getProName(), Toast.LENGTH_SHORT).show();
//                }
//            });
//            return convertView;
//        }
//
//        class ViewHolder {
//            private TextView proName;
//            private ImageView imgUrl;
//        }
//    }
//
//    /**
//     * @param 同步请求拿视屏数据放到gridview里面
//     *
//     */
////    public String posthttpresult(String path) {
////
////        String result = null;
////        OkHttpClient client = new OkHttpClient();
////        Gson gson = new Gson();
////        String resultStatus=null;
////        MediaType JSON = MediaType.parse("application/json; charset = utf-8");
////        FormBody body = new FormBody.Builder()
////                .add("gid", "1")
////                .build();
////        Request request = new Request.Builder()
////                .url(path)
////                .post(body)
////                .build();
////        try {
////            Response response = client.newCall(request).execute();
////            result = response.body().string();
////            GetUrlData getUrlData = gson.fromJson(result, GetUrlData.class);
////            Log.i(TAG, "最初为：" + getUrlData.getValues());
////            UrlData urlData = getUrlData.getValues();
////            resultStatus = getUrlData.getStatus();
////            Log.i(TAG, "数据为：" + urlData.getDatas());
////            try {
////                List<ListUrlData> listdata = urlData.getDatas();
////                for (int i = 0; i < listdata.size(); i++) {
////                    String valus = listdata.get(i).getValue();
////                    String status = listdata.get(i).getStatus();
////                    String outer_url = listdata.get(i).getOuter_url();
////                    ListUrlData p = new ListUrlData(valus, outer_url, status);
////                    listurl.add(p);
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////                Toast.makeText(getContext(), "网络无连接", Toast.LENGTH_SHORT).show();
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////            Toast.makeText(getContext(), "网络无连接", Toast.LENGTH_SHORT).show();
////
////        }
////        return resultStatus;
////
////    }
//}