package com.theagriculture.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class Initial_page extends AppCompatActivity {

    Button report_fire,log_in;
    TextView sign_up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_page);

        report_fire = findViewById(R.id.report_fire);
        log_in = findViewById(R.id.log_in);
        sign_up = findViewById(R.id.sign_up);

        report_fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Initial_page.this,ReportFire.class);
                startActivity(intent);
               // Toast.makeText(Initial_page.this, "report clicked", Toast.LENGTH_SHORT).show();
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Initial_page.this,login_activity.class);
                startActivity(intent);
                finish();
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Initial_page.this,RegistrationActivity.class);
                startActivity(i);
            }
        });
    }
}