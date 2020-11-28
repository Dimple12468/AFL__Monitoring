package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.Globals;
import com.theagriculture.app.ProfilePage;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.os.Trace.isEnabled;
import static com.android.volley.VolleyLog.TAG;

public class SetRadius extends AppCompatActivity {
    TextView radius_value;
    ImageButton edit;
    LinearLayout LLshowRadius,LLeditRadius;
    Button saveRadius;
    Switch notificationSwitch,adoSwitch,ddaSwitch;
    EditText RadiusSending;
    String TAG = "SetRadius",gotRadius;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_radius);

        LLshowRadius = findViewById(R.id.ll_radius);
        LLeditRadius = findViewById(R.id.ll_radius_edit);
        radius_value = findViewById(R.id.radius_value);
        edit = findViewById(R.id.radius_edit);
        saveRadius = findViewById(R.id.save_radius_edit);
        RadiusSending = findViewById(R.id.radius_value_edit);

        notificationSwitch= findViewById(R.id.notification_switch);
        ddaSwitch= findViewById(R.id.dda_switch);
        adoSwitch= findViewById(R.id.ado_switch);

        //get address
        SharedPreferences preferences = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        String state = preferences.getString("Address","");
        //Toast.makeText(getApplicationContext(),"state is "+state,Toast.LENGTH_LONG).show();
        Log.d(TAG,"state is "+state);

        gotRadius = Globals.url_Radius + state.toLowerCase() + "/";
        //////gotRadius = "https://api.aflmonitoring.com/api/Radius/haryana/";
        Log.d(TAG,"url is "+gotRadius);

        getRadius(gotRadius);

        /*

        String gotRadius1 = "https://api.aflmonitoring.com/api/Radius/Haryana/";
        Log.d(TAG,"url is "+gotRadius1);

        getRadius(gotRadius1);

        String gotRadius2 = "https://api.aflmonitoring.com/api/Radius/HARYANA/";
        Log.d(TAG,"url is "+gotRadius2);

        getRadius(gotRadius2);

         */

        //App bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar_setting);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);////for title change

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_back_24);

        TextView title_top = findViewById(R.id.app_name);
        title_top.setText("Advanced Settings");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetRadius.super.onBackPressed();
            }
        });




        //edit button
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),"Edit clicked",Toast.LENGTH_LONG).show();
                LLshowRadius.setVisibility(View.GONE);
                LLeditRadius.setVisibility(View.VISIBLE);

            }
        });


        //save radius button
        saveRadius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveRadius.setText("Sending...");
                sendRadiusRequest(gotRadius);

            }
        });


        notificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(getApplicationContext(),"Notifications Enabled",Toast.LENGTH_LONG).show();
                } else {
                    // The toggle is disabled
                    Toast.makeText(getApplicationContext(),"Notifications Disabled",Toast.LENGTH_LONG).show();
                }
            }
        });

        adoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(getApplicationContext(),"ADO Enabled",Toast.LENGTH_LONG).show();
                } else {
                    // The toggle is disabled
                    Toast.makeText(getApplicationContext(),"ADO Disabled",Toast.LENGTH_LONG).show();
                }
            }
        });

        ddaSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    Toast.makeText(getApplicationContext(),"DDA Enabled",Toast.LENGTH_LONG).show();
                } else {
                    // The toggle is disabled
                    Toast.makeText(getApplicationContext(),"DDA Disabled",Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    public void getRadius(final String url) {
        Log.d("get","enterd function");
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG,"response is "+response.toString() + " from url "+ url);
                        //Toast.makeText(getApplicationContext(),"response is "+response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            String radius = rootObject.getString("Radius");
                            radius_value.setText(radius + "km");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(),"An exception occurred",Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),error.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(), "Please Check your internet connection", Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getApplicationContext(), "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again", Toast.LENGTH_LONG).show();
                        }

                        Log.d(TAG, "onErrorResponse: " + error.getMessage());
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
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
        requestQueue.add(jsonObjectRequest);
    }

    public void sendRadiusRequest(final String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        JSONObject postParams = new JSONObject();
        try {
            String sendRadius_value = RadiusSending.getText().toString().trim();
            postParams.put("radius",sendRadius_value);
            Log.d(TAG,"posting value "+postParams.toString() + " to url " + url);
            Toast.makeText(getApplicationContext(), "Getting out of try1 wth postparamas "+postParams.toString(), Toast.LENGTH_LONG).show();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "An exception occured while doing post parameters", Toast.LENGTH_LONG).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,url, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG,"response from put request is "+response.toString());
                        Toast.makeText(getApplicationContext(),"Successfully submitted",Toast.LENGTH_LONG).show();
                        LLshowRadius.setVisibility(View.VISIBLE);
                        LLeditRadius.setVisibility(View.GONE);
                        radius_value.setText("...km");
                        saveRadius.setText("Send");
                        getRadius(url);

                        //Toast.makeText(getApplicationContext(),"response is "+response.toString(),Toast.LENGTH_SHORT).show();
                        //Process os success response
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG,"error is "+error.getMessage());
                //VolleyLog.e("Error: ", error.getMessage());
                Toast.makeText(getApplicationContext(),error.getMessage().toString(),Toast.LENGTH_LONG).show();
                if (error instanceof NoConnectionError || error instanceof TimeoutError){
                    Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getApplicationContext(), "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getApplicationContext(), "This error is case3", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getApplicationContext(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getApplicationContext(), "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(),"An error occured, retry again",Toast.LENGTH_LONG).show();
                LLshowRadius.setVisibility(View.VISIBLE);
                LLeditRadius.setVisibility(View.GONE);
                saveRadius.setText("Send");
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("key", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
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
        requestQueue.add(jsonObjectRequest);
    }
}
