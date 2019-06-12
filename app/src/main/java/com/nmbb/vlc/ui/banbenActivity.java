package com.nmbb.vlc.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.nmbb.vlc.R;

/**
 * Created by moonshine on 2018/4/25.
 */
public class banbenActivity extends Activity {
    public void onCreate(Bundle savdInstanceStated) {
        super.onCreate(savdInstanceStated);
        setContentView(R.layout.banben_layout);
        ImageView imageView = findViewById(R.id.returnview2);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("index","3");
                intent.setClass(banbenActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.down_in, R.anim.down_out);
    }
}
