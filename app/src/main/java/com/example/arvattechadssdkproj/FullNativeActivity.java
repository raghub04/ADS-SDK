package com.example.arvattechadssdkproj;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.arvtech.adssdk.adUtils.nativeAd.NativeUtilsBig;

public class FullNativeActivity extends AppCompatActivity {
    boolean isLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_native);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isLoaded = true;
                ((ImageView) findViewById(R.id.iv_close)).setVisibility(View.VISIBLE);
            }
        },1500);
        ((ImageView) findViewById(R.id.iv_close)).setOnClickListener(v -> finish());

        NativeUtilsBig.load_native(this,findViewById(com.arvtech.adssdk.R.id.rl_native),findViewById(com.arvtech.adssdk.R.id.tv_space),1);
    }

    @Override
    public void onBackPressed() {
        if (isLoaded) {
            super.onBackPressed();
        }
    }
}