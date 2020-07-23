package com.theagriculture.app.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.theagriculture.app.R;

public class no_internet extends AppCompatActivity {
    TextView close;
    Button retry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_internet);

        close = findViewById(R.id.close);
        retry = findViewById(R.id.retry);
    }
}
