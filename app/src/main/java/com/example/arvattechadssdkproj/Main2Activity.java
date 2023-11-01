package com.example.arvattechadssdkproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.arvtech.adssdk.ArvatAds;
import com.arvtech.adssdk.aditerface.Interstitial;

public class Main2Activity extends AppCompatActivity {
    Button showAds;
    RelativeLayout rlBanner,rl_native;
    View tv_space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showAds = findViewById(R.id.btn_next);
        rlBanner = findViewById(R.id.rl_banner);
        rl_native = findViewById(com.arvtech.adssdk.R.id.rl_native);
        tv_space = findViewById(com.arvtech.adssdk.R.id.tv_space);

//        InfyOmAds.showBanner(this,rlBanner,1);
        ArvatAds.showNative(this,rl_native,tv_space,1, ArvatAds.AdTemplate.NATIVE_300);
        ArvatAds.loadPreInterstitial(1,this);

        showAds.setOnClickListener(v -> {
            ArvatAds.showInterstitial(2, this, new Interstitial() {
                @Override
                public void onAdClose(boolean isFail) {
                    startActivity(new Intent(Main2Activity.this,MainActivity3.class));
                }
            });
        });


    }

    @Override
    protected void onResume() {
        ArvatAds.loadPreInterstitial(1,this);
        super.onResume();
    }
}