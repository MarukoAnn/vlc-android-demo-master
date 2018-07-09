package com.nmbb.vlc.modle;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.nmbb.vlc.R;

import java.util.List;

/**
 * Created by User on 2017/12/9.
 */

public class gridAdapter extends BaseAdapter{
    //上下文对象
    private Context context;
    //图片数组
    private Integer[] imgs;
    private  int index = 1;
    public gridAdapter(Context context, List imgs, int index){
        this.context = context;
        if (this.index == index)
        {
            this.imgs= new Integer[]{R.drawable.image, R.drawable.image,
                    R.drawable.image, R.drawable.image,
                    R.drawable.image, R.drawable.image,};
        }
        else {
            this.imgs= new Integer[]{R.drawable.img, R.drawable.img,
                    R.drawable.img, R.drawable.img,
                    R.drawable.img, R.drawable.img,};
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
