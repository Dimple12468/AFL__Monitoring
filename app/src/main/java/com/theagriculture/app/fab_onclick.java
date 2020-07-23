package com.theagriculture.app;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class fab_onclick extends AppCompatActivity {

    private TextView tv1,tv2,tv3,tv4;
   // private Button tv2,tv3,tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_onclick);


        tv1 = findViewById(R.id.upload);
        tv2 = findViewById(R.id.location);
        tv3 = findViewById(R.id.email);
        tv4 = findViewById(R.id.cancel);
    }
}
