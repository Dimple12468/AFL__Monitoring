package com.theagriculture.app.Admin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class pending_details extends AppCompatActivity {

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
    private String TAG="pending_details";
    private String token;

    private TextView aname;
    private TextView aemail;
    private TextView anumber;
    private TextView aaddress;

    private TextView bname;
    private TextView bemail;
    private TextView bnumber;
    private TextView baddress;
    private Button editbutton;
    private Button reportbutton;

    private boolean both = false;
    private boolean isPending = false;
    private boolean isOngoing = false;
    boolean doubleBackToExitPressedOnce=false;


    //for back button on action bar
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        int id = getItemId();
//
//        if (id==android.R.id.home) {
//            finish();
//        }
        return true;
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_pending_details);
        Log.d(TAG,"in onCreate: ");

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            id = bundle.get("id").toString();                                                                     //id = intent.getStringExtra("id").toString();
            ado_name = bundle.get("ado_name").toString().toUpperCase();
            dda_name = bundle.get("dda_name").toString().toUpperCase();
            address_top = bundle.get("address_big").toString().toUpperCase();
            ado_id = bundle.get("ado_pk").toString();
            dda_id = bundle.get("dda_pk").toString();
            isPending = bundle.getBoolean("isPending");
            isOngoing = bundle.getBoolean("isOngoing");
        }
        else{
            Toast.makeText(this,"bundle is null",Toast.LENGTH_LONG).show();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.app__bar_pending);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setTitle("Location Details");

        aname= findViewById(R.id.adodetail_name2);
        aemail = findViewById(R.id.adodetail_email2);
        anumber = findViewById(R.id.adodetail_phone2);

        aaddress = findViewById(R.id.addr2);
        aaddress.setText(address_top);

        TextView title_top = findViewById(R.id.app_name);
//        if (view.isEnabled()){
        title_top.setText("Location Details");
//        }else {
//            title_top.setText("AFL Monitoring");
//        }

        bname = findViewById(R.id.ddadetail_name2);
        bemail = findViewById(R.id.ddadetail_email2);
        bnumber = findViewById(R.id.ddadetail_phone2);

        editbutton = findViewById(R.id.button2);
        reportbutton = findViewById(R.id.report_button);

        if(isOngoing){
            reportbutton.setVisibility(View.VISIBLE);
            reportbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(pending_details.this,ongoing_details.class);
                    intent.putExtra("id", id);
                    intent.putExtra("review_address_top",address_top);
                    startActivity(intent);

//                    Bundle bundle = new Bundle();
//                    bundle.putString("id", id);
//                    bundle.putString("review_address_top",address_top);
                    //bundle.putBoolean("isDdo", true);
                    //bundle.putBoolean("isAdmin", true);
                    //bundle.putBoolean("isComplete", true);

//                    ongoingDetailsFragment abc = new ongoingDetailsFragment();
//                    abc.setArguments(bundle);
//                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,abc).addToBackStack(null).commit();
                }
            });
        }

        editbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(pending_details.this,"You clicked",Toast.LENGTH_LONG).show();
            }
        });

        SharedPreferences prefs = this.getSharedPreferences("tokenFile", MODE_PRIVATE);
        token = prefs.getString("key", "");

        urlado = Globals.User + ado_id + "/";
        urldda = Globals.User + dda_id + "/";

        Log.d(TAG, "urlado: " + urlado);
        Log.d(TAG, "urldda: " + urldda);

        if(ado_name.equals("NOT ASSIGNED") && dda_name.equals("NOT ASSIGNED")){
            both = false;
            //todo
            setContentView(R.layout.fragment_nothing_toshow);
//            view.setBackground(this.getResources().getDrawable(R.drawable.nothing_clipboard));
        }else if(ado_name.equals("NOT ASSIGNED") && !(dda_name.equals("NOT ASSIGNED"))){
            both = false;
            loadDetails(urlado,false,urldda,true);
        }else if(!(ado_name.equals("NOT ASSIGNED")) && dda_name.equals("NOT ASSIGNED")){
            both = false;
            loadDetails(urlado,true,urldda,false);
        }else if(!(ado_name.equals("NOT ASSIGNED")) && !(dda_name.equals("NOT ASSIGNED"))){
            both = true;
            loadDetails(urlado,true,urldda,true);
        }

    }

    private void loadDetails(final String adourl, final boolean isado, final String urldda, final boolean isdda) {
        Log.d(TAG,"Loading location details....");
        if(isado){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, adourl, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                name = rootObject.getString("name");
                                number = rootObject.getString("number");
                                email = rootObject.getString("email");

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
                            } catch (JSONException e) {
                                Toast.makeText(pending_details.this, "an exception occurred", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: JSON EXCEPTION: getDetails " + e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getActivity(), "Faced error while loading data", Toast.LENGTH_LONG).show();
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                //This indicates that the reuest has either time out or there is no connection
                                //Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
                                final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(pending_details.this);
//                                    View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                                LayoutInflater li = LayoutInflater.from(pending_details.this);
                                View sheetView = li.inflate(R.layout.no_internet, null);
//                                    View sheetView = setContentView(R.layout.no_internet);
                                mBottomDialogNotificationAction.setContentView(sheetView);
                                mBottomDialogNotificationAction.setCanceledOnTouchOutside(false);
                                mBottomDialogNotificationAction.setCancelable(false);
                                mBottomDialogNotificationAction.show();

                                // Remove default white color background

                                FrameLayout bottomSheet = (FrameLayout) mBottomDialogNotificationAction.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                                bottomSheet.setBackground(null);


                                TextView close = sheetView.findViewById(R.id.close);
                                Button retry = sheetView.findViewById(R.id.retry);

                                retry.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        mBottomDialogNotificationAction.dismiss();
                                        //spinner.setVisibility(View.VISIBLE);
                                        loadDetails(adourl,isado,urldda,isdda);
                                    }
                                });

                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!doubleBackToExitPressedOnce) {
                                            doubleBackToExitPressedOnce = true;
                                            Toast toast = Toast.makeText(pending_details.this,"Tap on Close App again to exit app", Toast.LENGTH_LONG);
                                            toast.setGravity(Gravity.CENTER, 0, 0);
                                            toast.show();



                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    doubleBackToExitPressedOnce = false;
                                                }
                                            }, 3600);
                                        } else {
                                            mBottomDialogNotificationAction.dismiss();
                                            Intent a = new Intent(Intent.ACTION_MAIN);//will exit app
                                            a.addCategory(Intent.CATEGORY_HOME);
                                            a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(a);
                                        }
                                    }

                                });


                            } else if (error instanceof AuthFailureError) {
                                // Error indicating that there was an Authentication Failure while performing the request
                                Toast.makeText(pending_details.this, "This error is case2", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                //Indicates that the server responded with a error response
                                Toast.makeText(pending_details.this, "This error is case3", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                //Indicates that there was network error while performing the request
                                Toast.makeText(pending_details.this, "This error is case4", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                // Indicates that the server response could not be parsed
                                Toast.makeText(pending_details.this, "This error is case5", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(pending_details.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                            }
                            Log.d(TAG, "onErrorResponse: getDetails " + error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(pending_details.this);
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
            //requestFinished(requestQueue);
        }
        if(isdda){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urldda, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                name = rootObject.getString("name");
                                number = rootObject.getString("number");
                                email = rootObject.getString("email");
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

                            } catch (JSONException e) {
                                Toast.makeText(pending_details.this, "an exception occured", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: JSON EXCEPTION: getDetails " + e);
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getActivity(), "Faced error while loading data", Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "onErrorResponse: getDetails " + error);
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                //This indicates that the reuest has either time out or there is no connection
                                // Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();

                            } else if (error instanceof AuthFailureError) {
                                // Error indicating that there was an Authentication Failure while performing the request
                                Toast.makeText(pending_details.this, "This error is case2", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                //Indicates that the server responded with a error response
                                Toast.makeText(pending_details.this, "This error is case3", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                //Indicates that there was network error while performing the request
                                Toast.makeText(pending_details.this, "This error is case4", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                // Indicates that the server response could not be parsed
                                Toast.makeText(pending_details.this, "This error is case5", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(pending_details.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                            }
                            /*
                        if (error instanceof ClientError) {
                            if (isado) {
                                Toast.makeText(getActivity(), "ado not assigned", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "dda not assigned", Toast.LENGTH_LONG).show();
                            }
                        } else {

                        }

                         */
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(pending_details.this);
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
        }

    }


}
