package com.theagriculture.app.Dda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.Admin.RecyclerViewAdapter_district;
import com.theagriculture.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsDialog extends Dialog implements View.OnClickListener {


    /*
    public DetailsDialog(Context context, RecyclerViewAdapter_district themeResId) {
        super(context, themeResId);
    }

     */

    public DetailsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    public Context activity;
    public Dialog dialog;
    public Button yes, no;
    TextView title;
    String adoName;
    String locationid;
    String adoid;
    String urlpatch;
    String token;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter adapter;


    public DetailsDialog(Context a, RecyclerView.Adapter adapter,String adoName, String locationid, String adoid ) {
        super(a);
        this.activity = a;
        this.adapter = adapter;
        this.adoName = adoName;
        this.locationid= locationid;
        this.adoid = adoid;
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.details_dialog);
        yes = (Button) findViewById(R.id.assign);
        no = (Button) findViewById(R.id.cancel);
        title = findViewById(R.id.name);
        title.setText(adoName);
        recyclerView = findViewById(R.id.list);
        mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);


        recyclerView.setAdapter(adapter);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.assign:
                assignAdo();
                //Toast.makeText(activity,"assign clicked",Toast.LENGTH_LONG).show();
                //Do Something
                break;
            case R.id.cancel:
                //Toast.makeText(activity,"cancel clicked",Toast.LENGTH_LONG).show();
                dismiss();
                break;
            default:
                break;
        }
        //dismiss();

    }

    public void assignAdo(){
        SharedPreferences preferences = activity.getSharedPreferences("tokenFile",Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        //Toast.makeText(activity,token,Toast.LENGTH_LONG).show();
        showdialogbox("", "Press OK for confirmation", "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                final JSONObject postbody = new JSONObject();
                try {
                    postbody.put("ado", adoid);
                } catch (JSONException e) {
                    Toast.makeText(activity,"An exception occured while making post body",Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
                final RequestQueue requestQueue = Volley.newRequestQueue(activity);
                urlpatch = "http://api.theagriculture.tk/api/location/" + locationid + "/";
                //Toast.makeText(activity,"url is "+urlpatch+" postbody is "+postbody.toString()+" toke is "+token,Toast.LENGTH_LONG).show();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PATCH, urlpatch, postbody, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(mcontext,"Location assigned",Toast.LENGTH_SHORT).show();
                        //((Activity)mcontext).finish();
                        try {
                            JSONObject c = new JSONObject(String.valueOf(response));
                            Toast.makeText(activity,"Location assigned",Toast.LENGTH_SHORT).show();
                            dismiss();
                            //Log.d(TAG, "onResponse: " + c);
                        }catch (JSONException e){
                            Toast.makeText(activity,"An exception occured "+e.getMessage(),Toast.LENGTH_LONG).show();
                            //Log.d(TAG, "onResponse: "+e);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.d(TAG, "onErrorResponse: "+error);
                        Toast.makeText(activity,"Ado not assigned.Please try again "+error.getMessage(),Toast.LENGTH_LONG).show();
                        if(error instanceof TimeoutError || error instanceof NoConnectionError){
                            Toast.makeText(activity, "This error is case1", Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(activity, "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(activity, "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(activity, "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(activity, "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(activity, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() {
                        HashMap<String, String> headers = new HashMap<>();
                        headers.put("Authorization", "Token " + token);
                        return headers;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            }
        }, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        },false);


    }

    //made function to show dialogbox
    private AlertDialog showdialogbox(String title, String msg, String positiveLabel, DialogInterface.OnClickListener positiveOnclick,
                                      String negativeLabel, DialogInterface.OnClickListener negativeOnclick,
                                      boolean isCancelable){

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(msg);
        builder.setPositiveButton(positiveLabel, positiveOnclick);
        builder.setNegativeButton(negativeLabel, negativeOnclick);
        builder.setCancelable(isCancelable);
        AlertDialog alert = builder.create();
        //Log.d(TAG, "showdialogbox: "+alert);
        alert.show();
        return alert;
    }


}
