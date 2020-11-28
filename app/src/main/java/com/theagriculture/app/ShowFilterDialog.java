package com.theagriculture.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.CustomDialog;
import com.theagriculture.app.Admin.RecyclerViewAdapter_district;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShowFilterDialog{// extends AppCompatActivity {
    Context Context;

    /*
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    */

    //function for alert dialog of filter search
    public void alert_filter_dialog(Context context) {
        Context = context;
        //Toast.makeText(Context,"You clicked",Toast.LENGTH_LONG).show();


        LayoutInflater li = LayoutInflater.from(Context);
        final View promptsView = li.inflate(R.layout.filter_dialog, null);

        //Create views for spinners here
        Spinner sp1 = promptsView.findViewById(R.id.status_filter_spinner);
        Spinner sp2 = promptsView.findViewById(R.id.date_filter_spinner);
        Button sp3 = promptsView.findViewById(R.id.state_filter_button);
        Button sp4 = promptsView.findViewById(R.id.district_filter_button);
        Button sp5 = promptsView.findViewById(R.id.village_filter_button);

        String[] status = Context.getResources().getStringArray(R.array.status);
        String[] date = Context.getResources().getStringArray(R.array.date);
        String[] district = {"Any", "Multiple"};
        String[] village = {"Any"};

        ArrayAdapter<String> status_adapter = new ArrayAdapter<String>(Context,android.R.layout.simple_spinner_item,status);
        status_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp1.setAdapter(status_adapter);
        sp1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> date_adapter = new ArrayAdapter<String>(Context,android.R.layout.simple_spinner_item,date);
        date_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp2.setAdapter(date_adapter);
        sp2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog();
                //Toast.makeText(getActivity(),"You clicked button",Toast.LENGTH_LONG).show();
            }
        });

        sp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog();
                //Toast.makeText(getActivity(),"You clicked button",Toast.LENGTH_LONG).show();
            }
        });

        sp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectionDialog();
                //Toast.makeText(getActivity(),"You clicked button",Toast.LENGTH_LONG).show();
            }
        });

        final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(Context);
        alertDialogBuilder.setTitle("Filter Search");
        alertDialogBuilder.setPositiveButton("Apply",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,
                                int which) {
                Toast.makeText(Context, "Apply is clicked", Toast.LENGTH_SHORT).show();
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Toast.makeText(getActivity(),"cancel is clicked",Toast.LENGTH_LONG).show();
                alertDialogBuilder.setCancelable(true);
            }
        });


        alertDialogBuilder.setView(promptsView);
        alertDialogBuilder.setCancelable(true);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Context.getResources().getColor(R.color.black));
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Context.getResources().getColor(R.color.black));
//        alertDialog.setCanceledOnTouchOutside(false);


    }

    public void showSelectionDialog(){
        CustomDialog customDialog;

        final ArrayList<String> items = new ArrayList<>();

        //fetch data

        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");
        items.add("Apple Apple Apple ");

        //RecyclerViewAdapter_district dataAdapter = new RecyclerViewAdapter_district(Context,items);
        AlternateColorAdapter dataAdapter = new AlternateColorAdapter(Context,items);
        customDialog = new CustomDialog((Activity) Context, dataAdapter);

        customDialog.show();
        customDialog.setCanceledOnTouchOutside(false);

    }

    public void getData(final ArrayList<String> mdistrictlist){
        RequestQueue district_requestQueue = Volley.newRequestQueue(Context);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, Globals.districtUrl, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject singleObject = response.getJSONObject(i);
                        mdistrictlist.add(singleObject.getString("district"));
                    } catch (JSONException e) {
                        Toast.makeText(Context,"You encountered an exception",Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(Context, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences preferences = Context.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = preferences.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        district_requestQueue.add(jsonArrayRequest);


    }
}
