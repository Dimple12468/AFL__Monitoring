package com.theagriculture.app.Dda;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;
import com.theagriculture.app.ReportFire;
import com.theagriculture.app.villageNameFragmentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

import static android.os.Trace.isEnabled;

public class adounderddointent extends AppCompatActivity {

    private String token;
    private ArrayList<String> villagesNames;
    private ArrayList<Integer> villageIds;
    private ArrayList<Integer> currentVillages;
    private ArrayList<String> suggestedVillageNames;
    private ArrayList<Integer> suggestedVillageIds;
    String mAdoId;
    private String mUrl;
    private String nextUrl;
    private boolean isNextBusy = false;
    StringBuffer name= null;
    StringBuffer id = null;
    LinearLayoutManager layoutManager;
    villageNameFragmentAdapter adapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProgressBar listNextProgressBar;
    SparseBooleanArray villagesIdSparse;
    ArrayList<Integer> selectedIds = new ArrayList<>();
    AlertDialog reportSubmitLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adounderddointent);

        recyclerView = findViewById(R.id.chk_recycler_dda);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_tick_chk_dda);
        progressBar = (ProgressBar) findViewById(R.id.village_name_loading);
        listNextProgressBar = findViewById(R.id.village_name_loadingnext);


        villagesNames = new ArrayList<>();
        villageIds = new ArrayList<>();
        suggestedVillageNames = new ArrayList<>();
        suggestedVillageIds = new ArrayList<>();



        /*
        Toolbar toolbar = findViewById(R.id.app__bar_ado_list);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getApplicationContext();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

         */


        TextView title_top = findViewById(R.id.app_name);
        title_top.setText("List of Villages");
        /*
        if (getApplicationContext().isEnabled()){
            title_top.setText("List of villages");
        }else {
            title_top.setText("AFL Monitoring");
        }

         */



        //////get id left
        Intent intent = getIntent();
        String districtId = intent.getStringExtra("districtId");
        mAdoId = intent.getStringExtra("adoId");
        currentVillages = intent.getIntegerArrayListExtra("currentVillages");
        Log.d("intent","got data from intent "+districtId + mAdoId + currentVillages.toString());

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"You clicked fab",Toast.LENGTH_LONG).show();
                //just uncomment
                for(Integer p:adapter.checkedVillageId){
                    selectedIds.add(p);
                }
                saveChanges();

            }
        });
        //mUrl = Globals.villageNameFragment + "2/";

        mUrl = Globals.villageNameFragment + districtId + "/";
        Log.d("murl",mUrl);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("key", "");
        progressBar.setVisibility(View.VISIBLE);
        loadData(mUrl);

        layoutManager = new LinearLayoutManager(this);
        adapter = new villageNameFragmentAdapter(this,villagesNames,villageIds);

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void loadData(String url) {
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                        Log.d("response",response.toString());
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            //Toast.makeText(getApplicationContext(),resultsArray.toString(),Toast.LENGTH_LONG).show();
                            Log.d("result",resultsArray.toString());
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject villageObject = resultsArray.getJSONObject(i);
                                String villageName = villageObject.getString("village");
                                villagesNames.add(villageName);
                                int villageId = villageObject.getInt("id");
                                villageIds.add(villageId);
                                /*
                                if (currentVillages.contains(villageId)) {
                                    adapter.addtoCurrentVillagesPos(villageId);
                                    Log.d(TAG, "onResponse: loaddata " + currentVillages);
                                }

                                 */
                            }
                            progressBar.setVisibility(View.GONE);
                            listNextProgressBar.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                            isNextBusy = false;
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressBar.setVisibility(View.GONE);
                            listNextProgressBar.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(), "Please try again", Toast.LENGTH_SHORT).show();
                            Log.d("getdata", "onResponse: JSON EXCEPTION " + e);
                            isNextBusy = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getApplicationContext(), "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                        Log.d("getdata", "onErrorResponse: loadData " + error);
                        isNextBusy = false;
                        //progressBar.setVisibility(View.GONE);
                        //listNextProgressBar.setVisibility(View.GONE);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            int totalCount, pastCount, visibleCount;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastCount = layoutManager.findFirstVisibleItemPosition();
                    visibleCount = layoutManager.getChildCount();
                    if ((pastCount + visibleCount) >= totalCount) {
                        if (!nextUrl.equals("null") && !isNextBusy) {
                            String imagelink=nextUrl;
                            Log.d("imagelink1",imagelink);
                            String newImageLink = " ";

                            char anc = imagelink.charAt(4);
                            int comp = Character.compare(anc, 's');
                            if(comp!=0){
                                newImageLink = imagelink;
                                newImageLink =  newImageLink.substring(4);
                                newImageLink = "https" + newImageLink;
                            }
                            Log.d("response","new link is "+newImageLink);
                            listNextProgressBar.setVisibility(View.VISIBLE);
                            loadData(newImageLink);

                        }
                        Log.d("getdata", "onScrolled:");
                    }
                    Log.d("getdata", "onScrolled: " + totalCount + "total" + pastCount + "past" + visibleCount + "visible");
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void saveChanges() {
        if(selectedIds.size()!=0){
            /////////

            /*not working
            reportSubmitLoading = new SpotsDialog.Builder().setContext(getApplicationContext()).setMessage("Submitting Report")
                    .setTheme(R.style.CustomDialog)
                    .setCancelable(false)
                    .build();
            reportSubmitLoading.show();

             */


            //
            JSONArray villagesIdArray = new JSONArray(selectedIds);
            JSONObject paramObject = new JSONObject();
            try {
                paramObject.put("village", villagesIdArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d("save changes", "saveChanges: " + paramObject);
            //String url = "http://api.theagriculture.tk/api/user/" + mAdoId + "/";
            String url = Globals.sendSelectedId + mAdoId + "/";
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, url, paramObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                Log.d("save chnages", "onResponse: " + jsonObject);
                                Toast.makeText(getApplicationContext(), "Villages Successfully Assigned!", Toast.LENGTH_SHORT).show();
                                //////////reportSubmitLoading.dismiss();
                                Intent intent = new Intent(getApplicationContext(), DdaActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("isAssignedLocation", true);
                                startActivity(intent);
                                //finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                /////////reportSubmitLoading.dismiss();
                                Toast.makeText(getApplicationContext(),"An exception occured",Toast.LENGTH_LONG).show();
                                Log.d("save changes", "onResponse: JSON EXCEPTION " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            ///////reportSubmitLoading.dismiss();
                            if (error instanceof NoConnectionError)
                                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_LONG).show();
                            else if (error instanceof AuthFailureError) {
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
                            else
                                Toast.makeText(getApplicationContext(), "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                            Log.d("save changes", "onErrorResponse: loadData " + error);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Authorization", "Token " + token);
                    return map;
                }

                @Override
                public String getBodyContentType() {
                    return "application/json";
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            jsonArrayRequest.setRetryPolicy(new RetryPolicy() {
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
            requestQueue.add(jsonArrayRequest);
        } else
            Toast.makeText(getApplicationContext(), "No village Selected", Toast.LENGTH_SHORT).show();
    }
}
