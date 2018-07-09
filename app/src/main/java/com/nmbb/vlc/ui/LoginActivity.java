package com.nmbb.vlc.ui;

/**
 * Created by User on 2017/11/27.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.nmbb.vlc.R;

/**
 * Created by Administrator on 2017/11/7 0007.
 */

public class LoginActivity extends Activity {
    private Button login;
    EditText username;
    EditText password;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.button);
        username = super.findViewById(R.id.username);//获取用户名
        password = super.findViewById(R.id.password);//获取密码
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //验证用户密码是否符合要求
                if(username.getText().toString().equals("admin")&&password.getText().toString().equals("88888")){
                    //提示用户登录成功
                    Toast.makeText(getApplicationContext(),"登录成功",Toast.LENGTH_SHORT).show();
                    //从login界面跳转到main界面
                    Intent t=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(t);
                }
                if(!username.getText().toString().equals("admin")||!password.getText().toString().equals("88888")){
                    //提示用户名或密码错误
                    Toast.makeText(getApplicationContext(),"用户名或密码错误",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }}