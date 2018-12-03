package com.nmbb.vlc.Util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * @author Moonshine QQ:710159940
 * @name Zxing
 * @class name：com.example.moonshine.zxing.Util
 * @class describe
 * @time 2018/11/24 13:35
 * @class describe
 */
public class SavePamasInfo {

    /**
     * 保存信息
     * @param context
     * @param key
     * @param value
     * @param filename
     */
    public void saveInfo(Context context, String key, String value, String filename){

        SharedPreferences sharedPreferences = context.getSharedPreferences(filename,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }

    /**
     * 获取信息
     * @param context
     * @param key
     * @param filename
     * @return
     */
    public String getInfo(Context context, String key, String filename){
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,"");
    }
}
