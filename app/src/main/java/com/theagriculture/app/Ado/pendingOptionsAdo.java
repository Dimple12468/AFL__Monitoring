package com.theagriculture.app.Ado;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.theagriculture.app.R;

public class pendingOptionsAdo extends AppCompatActivity {

    private TextView tv1,tv2,tv4;
    // private Button tv2,tv3,tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pending_options_ado);


        tv1 = findViewById(R.id.check);
        tv2 = findViewById(R.id.navigation);
        tv4 = findViewById(R.id.cancel_ado);
    }
}
