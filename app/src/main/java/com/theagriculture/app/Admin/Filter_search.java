package com.theagriculture.app.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.theagriculture.app.R;

import java.util.ArrayList;
import java.util.List;

public class Filter_search extends AppCompatActivity /*implements AdapterView.OnItemSelectedListener*/ {

    TextView tv1,tv2,tv3,tv4,tv5;
    Spinner sp1,sp2,sp3,sp4,sp5;
    String[] date = getResources().getStringArray(R.array.date);
    String[] status = getResources().getStringArray(R.array.status);
    String[] state = { "list1", "list2"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_search);

        tv1 = findViewById(R.id.status_filter);
        tv2 = findViewById(R.id.date_filter);
        tv3 = findViewById(R.id.state_filter);
        tv4 = findViewById(R.id.district_filter);
        tv5 = findViewById(R.id.village_filter);

        sp1 = findViewById(R.id.status_filter_spinner);
        sp2 = findViewById(R.id.date_filter_spinner);
        sp3 = findViewById(R.id.state_filter_spinner);
        sp4 = findViewById(R.id.district_filter_spinner);
        sp5 = findViewById(R.id.village_filter_spinner);


        //String[] district = { "Active", "Inactive"};
        //String[] village = { "Active", "Inactive"};


       // ArrayList<String> state_spinner = new ArrayList<String>(this,android.R.layout.simple_spinner_item,state);

      /*  ArrayAdapter<String> status_spinner = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,status);
        status_spinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setPrompt("Active");
        sp1.setAdapter(status_spinner);

        sp2.setPrompt("Any");
        sp3.setPrompt("Any");
        sp4.setPrompt("Any");
        sp5.setPrompt("Any");


        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               /* String state = null;
                if(position==0)
                    state = "Active";
                if(position==1)
                    state = "Inactive";
               String state = sp1.getSelectedItem().toString();
               //if (!state.equals("Inactive")){
                Toast.makeText(Filter_search.this, state, Toast.LENGTH_SHORT).show();
              // }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/
    }

   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int spinner_id = parent.getId();
        switch (spinner_id)
        {
            case R.id.status_filter_spinner:
                Toast.makeText(this, "hello in status", Toast.LENGTH_SHORT).show();
            case R.id.date_filter_spinner:
                Toast.makeText(this, "hello in date", Toast.LENGTH_SHORT).show();
            case R.id.state_filter_spinner:
                Toast.makeText(this, "hello in state", Toast.LENGTH_SHORT).show();
            case R.id.district_filter_spinner:
                Toast.makeText(this, "hello in district", Toast.LENGTH_SHORT).show();
            case R.id.village_filter_spinner:
                Toast.makeText(this, "hello in village", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/
}