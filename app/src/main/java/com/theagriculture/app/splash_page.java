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

import androidx.appcompat.app.AppCompatActivity;

import com.theagriculture.app.Admin.AdminActivity;

public class splash_page extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_page);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

//                SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
//                String typeofuser = preferences.getString("typeOfUser","");
//                String username = preferences.getString("Name","");

//                login_activity logged_in_user = new login_activity();
//                Intent i = new Intent( splash_page.this ,login_activity.class);
                Intent i = new Intent( splash_page.this ,Initial_page.class);
                startActivity(i);
                finish();
            }
        }, 2000);
    }
}
