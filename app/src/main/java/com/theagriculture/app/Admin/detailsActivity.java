package com.theagriculture.app.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.ClientError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.R;
import com.theagriculture.app.login_activity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class detailsActivity extends AppCompatActivity {

    private String ado_name;
    private String dda_name;
    private String address_top;
    private RelativeLayout parent;
    private TextView noDetails_text;
    private String id;
    private String urlado;
    private String urldda;
    private String ado_id;
    private String dda_id;

    private String name;
    private String number;
    private String email;
    private String address;
    private String TAG="Detail class";
    private String token;

    private TextView aname;
    private TextView aemail;
    private TextView anumber;
    private TextView aaddress;

    private TextView bname;
    private TextView bemail;
    private TextView bnumber;
    private TextView baddress;
    private ImageButton edit_button;

    private boolean both = false;
    /*

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_top_bar , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.logout) {
            SharedPreferences.Editor editor = getSharedPreferences("tokenFile", MODE_PRIVATE).edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(detailsActivity.this, login_activity.class);
            startActivity(intent);
            finish();
            return true;
        }
        if(id == R.id.help){

        }

        if(id == R.id.policy){

        }

        if(id == R.id.service){

        }

        if(id == R.id.mohit){

        }
        return super.onOptionsItemSelected(item);
    }

     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent intent = getIntent();

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_bar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle("DETAILS");
        ado_name = intent.getStringExtra("ado_name");
        dda_name = intent.getStringExtra("dda_name");
        address_top = intent.getStringExtra("address_big");
        ado_id = intent.getStringExtra("ado_pk");
        dda_id = intent.getStringExtra("dda_pk");
        parent = findViewById(R.id.parent);

        noDetails_text = findViewById(R.id.nodetail_text);

        aname= findViewById(R.id.adodetail_name2);
        aemail = findViewById(R.id.adodetail_email2);
        anumber = findViewById(R.id.adodetail_phone2);
        aaddress = findViewById(R.id.addr2);
        aaddress.setText(address_top);

        bname = findViewById(R.id.ddadetail_name2);
        bemail = findViewById(R.id.ddadetail_email2);
        bnumber = findViewById(R.id.ddadetail_phone2);
        baddress = findViewById(R.id.addr2);

        edit_button = findViewById(R.id.edit_button);

        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(detailsActivity.this,"You clicked",Toast.LENGTH_LONG).show();
            }
        });

        SharedPreferences prefs = getSharedPreferences("tokenFile", MODE_PRIVATE);
        token = prefs.getString("token", "");

        urlado = "http://18.224.202.135/api/user/" + ado_id + "/";
        urldda = "http://18.224.202.135/api/user/" + dda_id + "/";

        Log.d(TAG, "onCreate: "+ado_id);
        Log.d(TAG, "onCreate: "+dda_id);


        if(ado_name.equals("NOT ASSIGNED") && dda_name.equals("NOT ASSIGNED")){
            both = false;
            parent.setVisibility(View.GONE);
            noDetails_text.setVisibility(View.VISIBLE);
        }else if(ado_name.equals("NOT ASSIGNED") && !(dda_name.equals("NOT ASSIGNED"))){
            both = false;
            loadData(urldda,false);

        }else if(!(ado_name.equals("NOT ASSIGNED")) && dda_name.equals("NOT ASSIGNED")){
            both = false;
            loadData(urlado,true);
        }else if(!(ado_name.equals("NOT ASSIGNED")) && !(dda_name.equals("NOT ASSIGNED"))){
            both = true;
            loadData(urlado,true);
        }



    }

    private void loadData(String url , final boolean isado) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            name = rootObject.getString("name");
                            number = rootObject.getString("number");
                            email = rootObject.getString("email");
                            //address = rootObject.getString("address");

                            //Resources.getSystem().getString(R.string.blah);


                            if(isado){
                                //if (!address.isEmpty())
                                  //  aaddress.setText(address);
                                //else
                                  //  aaddress.setText("NONE");
                                if (!name.isEmpty())
                                    aname.setText(name);
                                else
                                    aname.setText("NONE");
                                if (!number.isEmpty())
                                    anumber.setText(number);
                                else
                                    anumber.setText("NONE");
                                if (!email.isEmpty())
                                    aemail.setText(email);
                                else
                                    aemail.setText("NONE");

                            }
                            else{
                                //if (!address_show.isEmpty())
                                  //  baddress.setText(address_show);
                                //else
                                  //  baddress.setText("NONE");
                                if (!name.isEmpty())
                                    bname.setText(name);
                                else
                                    bname.setText("NONE");
                                if (!number.isEmpty())
                                    bnumber.setText(number);
                                else
                                    bnumber.setText("NONE");
                                if (!email.isEmpty())
                                    bemail.setText(email);
                                else
                                    bemail.setText("NONE");

                            }

                           // adapter = new PendingAdapter();

                            //fetchSpinnerData(spinnerUrl);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "onResponse: JSON EXCEPTION: getDetails " + e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: getDetails " + error);
                        if (error instanceof ClientError) {
                            if (isado) {

                                Toast.makeText(getApplicationContext(), "ado not assigned",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "dda not assigned",
                                        Toast.LENGTH_LONG).show();

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong, try again later!",
                                    Toast.LENGTH_LONG).show();


                        }


                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        requestFinished(requestQueue);

    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if(both){
                    loadData(urldda,false);
                }
            }
        });

    }

    /*@Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }*/
}
