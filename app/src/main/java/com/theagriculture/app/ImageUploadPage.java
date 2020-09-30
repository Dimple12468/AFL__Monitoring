package com.theagriculture.app;

import android.app.AlertDialog;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.nlopez.smartlocation.OnLocationUpdatedListener;
import io.nlopez.smartlocation.SmartLocation;

public class ImageUploadPage extends AppCompatActivity {
    Location userLocation;
    AlertDialog reportSubmitLoading;
    String reportSubmitUrl = "https://api.aflmonitoring.com/api/report-user/add/";
    Button submitImages;
    FloatingActionButton opencamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_upload_page);

        reportSubmitLoading = new SpotsDialog.Builder().setContext(ImageUploadPage.this).setMessage("Submitting Report")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false)
                .build();
        reportSubmitLoading.show();

        SmartLocation.with(getApplicationContext()).location().start(new OnLocationUpdatedListener() {
            @Override
            public void onLocationUpdated(Location location) {
                userLocation = location;
                Toast.makeText(getApplicationContext(),userLocation.toString(),Toast.LENGTH_LONG).show();
                sendReport();
            }
        });

        submitImages = findViewById(R.id.submit_report);
        opencamera = findViewById(R.id.camera);

        submitImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Synjnd",Toast.LENGTH_LONG).show();
            }
        });

        opencamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"nabss",Toast.LENGTH_LONG).show();
            }
        });

    }

    public void sendReport() {


        // Toast.makeText(getApplicationContext(), "Entered reprt function", Toast.LENGTH_LONG).show();

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        JSONObject postParams = new JSONObject();
        try {
            postParams.put("longitude", "76.8498");
            postParams.put("latitude", "28.2314");
            ////postParams.put("longitude", String.valueOf(userLocation.getLongitude()));
            ////postParams.put("latitude", String.valueOf(userLocation.getLatitude()));
            postParams.put("name", "Nmae");
            postParams.put("phone_number", "Phonenumber");


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "An exception occured", Toast.LENGTH_LONG).show();
            reportSubmitLoading.dismiss();
            Log.d("catch", "submitReport: " + e);
        }
        Toast.makeText(getApplicationContext(),"posting values "+postParams.toString(),Toast.LENGTH_LONG).show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(reportSubmitUrl, postParams,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(getApplicationContext(),"response is "+response.toString(),Toast.LENGTH_LONG).show();
                            JSONObject singleObject = new JSONObject(String.valueOf(response));
                            String reportId = singleObject.getString("NormalUserReport_id");
                            Toast.makeText(getApplicationContext(),"report id is "+reportId,Toast.LENGTH_LONG).show();
                            Log.d("response", "onResponse: " + singleObject);
                            reportSubmitLoading.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            reportSubmitLoading.dismiss();
                            Log.d("catchq", "jsonexception: reportSubmitRequest " + e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"error is "+error.getMessage(),Toast.LENGTH_LONG).show();
                        if (error instanceof NoConnectionError || error instanceof TimeoutError){
                            Toast.makeText(getApplicationContext(), "Check your internet connection!", Toast.LENGTH_LONG).show();
                        }else if (error instanceof AuthFailureError) {
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
                        reportSubmitLoading.dismiss();
                    }
                });
        /*
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
               /// map.put("Authorization", "Token " +mytoken);
                return map;
            }
        };

         */


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
    }
}


