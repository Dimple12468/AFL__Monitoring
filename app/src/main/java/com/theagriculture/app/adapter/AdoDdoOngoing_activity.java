package com.theagriculture.app.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.theagriculture.app.Admin.AdoDdoActivity.AdoDdoListAdapter;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdoDdoOngoing_activity extends AppCompatActivity {

    private String mDdoId;
    private ArrayList<String> locationNames;
    private ArrayList<String> locationAddresses;
    private ArrayList<String> mAdoNames;
    private ArrayList<String> mIds;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private AdoDdoListAdapter adapter;
    private String nextUrl;
    private boolean isDdo;
    private String token;
    private String TAG = "adoddoongoing";
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar spinner;
    String mUrl;
    int no_of_visits = 0;


    public AdoDdoOngoing_activity() {
        // Required empty public constructor
    }


    public AdoDdoOngoing_activity(String mDdoId, boolean isDdo) {
        this.mDdoId = mDdoId;
        this.isDdo = isDdo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_ddo_ongoing);
        Log.d(TAG,"in onCreate: ");

        spinner = findViewById(R.id.Ddo_ongoing_loading_json);
        spinner.setVisibility(View.VISIBLE);
        swipeRefreshLayout = findViewById(R.id.refreshpull6);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                startActivity(AdoDdoOngoing_activity.this);
//                getFragmentManager().beginTransaction().detach(AdoDdoOngoing.this).attach(AdoDdoOngoing.this).commit();
            }
        });

        String role;
        if (isDdo)
            role = "dda";
        else
            role = "ado";
        mUrl = Globals.admin + role + "/" + mDdoId + "/ongoing";                              //"http://18.224.202.135/api/admin/" + role + "/" + mDdoId + "/ongoing";
        Log.d("url", "onCreateView: ongoing" + mUrl);

        progressBar = findViewById(R.id.Ddo_ongoing_loading);
        recyclerView = findViewById(R.id.Ddo_ongoing_recyclerview);
        layoutManager = new LinearLayoutManager(this);
        SharedPreferences prefs = this.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("token", "");
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        locationNames = new ArrayList<>();
        locationAddresses = new ArrayList<>();
        mAdoNames = new ArrayList<>();
        mIds = new ArrayList<>();
        adapter = new AdoDdoListAdapter(this, locationNames, locationAddresses, mAdoNames, mIds,
                true, 2);
        recyclerView.setAdapter(adapter);


        getData(mUrl);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        loadNextLocations(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

//    @Override
//    public void setUserVisibleHint(boolean isVisibletoUser){
//        super.setUserVisibleHint(isVisibletoUser);
//        if(isVisibletoUser && no_of_visits==0) {
//            //recyclerView.setAdapter(adapter);
//            getData(mUrl);
//        }
//    }


    private void getData(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @SuppressLint("InflateParams")
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            if (resultsArray.length() == 0) {
                                //adapter.mshowshimmer = false;
                                adapter.notifyDataSetChanged();
                                AdoDdoOngoing_activity.this.getLayoutInflater().inflate(R.layout.fragment_nothing_toshow, null);
//                                nothing_toshow_fragment no_data = new nothing_toshow_fragment();
//                                AppCompatActivity activity = (AppCompatActivity)getContext();
//                                //getFragmentManager().beginTransaction().detach(AdoDdoOngoing.this).attach(nothing_toshow_fragment).addToBackStack(null).commit();
//                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.change_nodata_completed, no_data).commit();
                                //view.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_group_217));
                            }
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                if (isDdo) {
                                    try {
                                        JSONObject adoObject = singleObject.getJSONObject("ado");
                                        String adoName = adoObject.getString("name");
                                        mAdoNames.add(adoName);
                                    } catch (JSONException e) {
                                        mAdoNames.add("Not Assigned");
                                    }
                                    String id = singleObject.getString("id");
                                    mIds.add(id);
                                }
                                String locName = singleObject.getString("village_name");
                                String locAdd = singleObject.getString("block_name") +
                                        ", " + singleObject.getString("district");
                                locationNames.add(locName);
                                locationAddresses.add(locAdd);
                            }
                            Log.d(TAG, "onResponse: NOTIFY " + mAdoNames + "    " + locationNames + "   " + locationAddresses);
                            //adapter.mshowshimmer = false;
                            adapter.notifyDataSetChanged();
                            spinner.setVisibility(View.GONE);
                            no_of_visits++;
                        } catch (JSONException e) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(AdoDdoOngoing_activity.this,"An exception occurred",Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spinner.setVisibility(View.GONE);
                        if (error instanceof NoConnectionError || error instanceof TimeoutError) {
                            //Toast.makeText(getActivity(), "Please Check your Internet " + "Connection!", Toast.LENGTH_SHORT).show();

                            final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(AdoDdoOngoing_activity.this);
                            View sheetView = AdoDdoOngoing_activity.this.getLayoutInflater().inflate(R.layout.no_internet, null);
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
                                    spinner.setVisibility(View.VISIBLE);
                                    getData(url);
                                }
                            });

                            close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (!doubleBackToExitPressedOnce) {
                                        doubleBackToExitPressedOnce = true;
                                        Toast toast = Toast.makeText(AdoDdoOngoing_activity.this,"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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

                        }
                        else if (error instanceof AuthFailureError) {
                            // Error indicating that there was an Authentication Failure while performing the request
                            Toast.makeText(AdoDdoOngoing_activity.this, "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(AdoDdoOngoing_activity.this, "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(AdoDdoOngoing_activity.this, "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(AdoDdoOngoing_activity.this, "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(AdoDdoOngoing_activity.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                        }
                        Log.d(TAG, "onErrorResponse: getData " + error);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private void loadNextLocations(final String url) {
        RequestQueue queue = Volley.newRequestQueue(this);
        Log.d(TAG, "getNextlocations: inside");
        if (!url.isEmpty()) {
            progressBar.setVisibility(View.VISIBLE);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject rootObject = new JSONObject(String.valueOf(response));
                                JSONArray resultsArray = rootObject.getJSONArray("results");
                                if (resultsArray.length() == 0) {
                                    //adapter.mshowshimmer = false;
                                    adapter.notifyDataSetChanged();
                                    AdoDdoOngoing_activity.this.getLayoutInflater().inflate(R.layout.fragment_nothing_toshow, null);

                                    //todo image here
//                                    nothing_toshow_fragment no_data = new nothing_toshow_fragment();
//                                    AppCompatActivity activity = (AppCompatActivity)getContext();
//                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.change_nodata_completed, no_data).commit();
                                    //view.setBackground(getActivity().getResources().getDrawable(R.drawable.svg_nothing_toshow_1));
                                    //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                                }
                                for (int i = 0; i < resultsArray.length(); i++) {
                                    JSONObject singleObject = resultsArray.getJSONObject(i);
                                    if (isDdo) {
                                        try {
                                            JSONObject adoObject = singleObject.getJSONObject("ado");
                                            String adoName = adoObject.getString("name");
                                            mAdoNames.add(adoName);
                                        } catch (JSONException e) {
                                            mAdoNames.add("Not Assigned");
                                        }
                                        String id = singleObject.getString("id");
                                        mIds.add(id);
                                    }
                                    String locName = singleObject.getString("village_name");
                                    String locAdd = singleObject.getString("block_name") +
                                            ", " + singleObject.getString("district");
                                    locationNames.add(locName);
                                    locationAddresses.add(locAdd);
                                    adapter.notifyDataSetChanged();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                                //This indicates that the reuest has either time out or there is no connection
                                //Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
                                final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(AdoDdoOngoing_activity.this);
                                View sheetView = AdoDdoOngoing_activity.this.getLayoutInflater().inflate(R.layout.no_internet, null);
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
                                        loadNextLocations(nextUrl);
                                    }
                                });

                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (!doubleBackToExitPressedOnce) {
                                            doubleBackToExitPressedOnce = true;
                                            Toast toast = Toast.makeText(AdoDdoOngoing_activity.this,"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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
                                Toast.makeText(AdoDdoOngoing_activity.this, "This error is case2", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                //Indicates that the server responded with a error response
                                Toast.makeText(AdoDdoOngoing_activity.this, "This error is server error", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                //Indicates that there was network error while performing the request
                                Toast.makeText(AdoDdoOngoing_activity.this, "This error is case4", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                // Indicates that the server response could not be parsed
                                Toast.makeText(AdoDdoOngoing_activity.this, "This error is case5", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(AdoDdoOngoing_activity.this, "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
            queue.add(jsonObjectRequest);
            requestFinished(queue);
        }

    }

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
