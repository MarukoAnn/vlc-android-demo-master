package com.nmbb.vlc.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nmbb.vlc.modle.SelectOid;
import com.nmbb.vlc.modle.SidSelectData;
import com.nmbb.vlc.modle.UserpassData;

import java.util.ArrayList;

/**
 * Created by moonshine on 2018/2/5.
 */

public class DataDBHepler extends SQLiteOpenHelper {

    public final static String dbname="data.db";
    public final static int dbversion = 7;

    public DataDBHepler(Context context) {
        super(context,dbname,null, dbversion);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table if not exists Sid_table(id INTEGER primary key autoincrement,sid varchar(256))");
        db.execSQL("create table if not exists User_table(id INTEGER primary key autoincrement,user varchar(256),name verchar(125),idnum verchar(125),birthday vercahr(125),phone verchar(125))");
        db.execSQL("create table if not exists Oid_table(id INTEGER primary key autoincrement,oid varchar(256))");
        db.execSQL("create table if not exists Password_table(id INTEGER primary key autoincrement,username verchar(125),password verchar(125))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }


    public boolean isIdorSid() {

        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT * FROM Sid_table";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(SQL,null);
        if (cursor.getCount()>0) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 查询用户名和密码
     * @return
     */

    public boolean isIdoruserpass() {
        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT * FROM Password_table";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(SQL,null);
        if (cursor.getCount()>0) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 查询数据库里是否有Oid
     * @return
     */

    public boolean isIdorOid() {

        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT * FROM Oid_table";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.getCount()>0) {
            return true;
        }else {
            return false;
        }
    }


    public boolean isIdorUser() {

        SQLiteDatabase db = this.getWritableDatabase();
        String SQL = "SELECT * FROM User_table";
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(SQL,null);

        if (cursor.getCount()>0) {
            return true;
        }else {
            return false;
        }
    }

    /**
     * 查询数据库里有没有 这个sid
     * @return
     */
    public ArrayList<SidSelectData> FindSidData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList SidList = new ArrayList();
        Cursor cursor = db.rawQuery("select * from Sid_table", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            SidSelectData info =new SidSelectData(cursor.getString(0),cursor.getString(1));
            SidList.add(info);
        }
        cursor.close();
        db.close();
        return SidList;
    }

    /**
     * 查询userpassword
     *
     */
    public ArrayList<UserpassData> FinduserpassData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList SidList = new ArrayList();
        Cursor cursor = db.rawQuery("select * from Password_table", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            UserpassData info =new UserpassData(cursor.getString(0),cursor.getString(1),cursor.getString(2));

            SidList.add(info);
        }
        cursor.close();
        db.close();
        return SidList;
    }
    /**
     *
     * 查询Oid
     */
    public ArrayList<SelectOid> FindOidData() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList SidList = new ArrayList();
        Cursor cursor = db.rawQuery("select * from Oid_table", null);
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();
            SelectOid info =new SelectOid(cursor.getString(0),cursor.getString(1));

            SidList.add(info);
        }
        cursor.close();
        db.close();
        return SidList;
    }

    //更新数据
    public void  update(String sid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Sid_table SET Sid = '" + sid +"' WHERE id= '1'");
    }
    //更新Oid
    public void  updateoid(String oid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Oid_table SET Oid = '" + oid +"' WHERE id= '1'");
    }


    //更新username，passsword
    public void  updateUserpass(String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("UPDATE Password_table SET username='"+user+"',password = '"+password+"'  WHERE id= '1'");
    }
    /**
     * 插入sid
     * @param id
     * @param sid
     */
    public void add(String id, String sid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into Sid_table(id,sid) values(?,?)",
                new Object[]{id,sid});
    }
    /**
     * 插入oid
     *
     */
    public void addoid(String id, String oid){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into Oid_table(id,Oid) values(?,?)",
                new Object[]{id,oid});
    }

    /**
     * 插入用户名User
     * @param id
     * @param user
     */
    public void addUser(String id, String user, String name, String idnum, String birthday, String phone){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into User_table(id,user,name,idnum,birthday,phone) values(?,?,?,?,?,?)",
                new Object[]{id,user});
    }
    //更新username，passsword
    public void  addUserpass(String id, String user, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into Password_table(id,username,password) values(?,?,?)");
    }

    /**+
     * 删除Sid_table里面的数据
     * @return
     */
    public void delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from Sid_table where id=?",new Object[]{id});
    }
}
