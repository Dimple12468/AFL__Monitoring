package com.theagriculture.app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class splash_page extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                Intent i = new Intent( splash_page.this ,login_activity.class);
                SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String typeofuser = preferences.getString("role","");
                String username = preferences.getString("Name","");
                Intent i;
                if(typeofuser.isEmpty()){
                    i = new Intent( splash_page.this ,Initial_page.class);
                }else
                    i = new Intent( splash_page.this ,login_activity.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
