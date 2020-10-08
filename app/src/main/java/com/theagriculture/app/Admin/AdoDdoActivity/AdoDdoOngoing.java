package com.theagriculture.app.Admin.AdoDdoActivity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
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
import com.theagriculture.app.Ado.AdoListAdapter;
import com.theagriculture.app.Globals;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AdoDdoOngoing extends Fragment {

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
    private View view;
    private String TAG = "adoddoongoing";
    private SwipeRefreshLayout swipeRefreshLayout;
    boolean doubleBackToExitPressedOnce = false;
    ProgressBar spinner;
    String mUrl;
    int no_of_visits = 0;
    ArrayList<String> auth_ado , auth_ddo, d_id ;

    public AdoDdoOngoing() {
        // Required empty public constructor
    }

    public AdoDdoOngoing(String mDdoId, boolean isDdo) {
        this.mDdoId = mDdoId;
        this.isDdo = isDdo;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibletoUser){
        super.setUserVisibleHint(isVisibletoUser);
        if(isVisibletoUser && no_of_visits==0) {
            //recyclerView.setAdapter(adapter);
            getData(mUrl);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ddo_ongoing, container, false);
        spinner = view.findViewById(R.id.Ddo_ongoing_loading_json);
        spinner.setVisibility(View.VISIBLE);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull6);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(AdoDdoOngoing.this).attach(AdoDdoOngoing.this).commit();
            }
        });
        Log.d(TAG, "onCreateView: yoyo" + view);
        String role;
        if (isDdo)
            role = "dda";
        else
            role = "ado";
        mUrl = Globals.admin + role + "/" + mDdoId + "/ongoing";                //"http://18.224.202.135/api/admin/" + role + "/" + mDdoId + "/ongoing";
        Log.d("url", "onCreateView: ongoing " + mUrl);

        //Toast.makeText(getActivity(),"got id="+ mDdoId + "isDdo="+isDdo,Toast.LENGTH_LONG).show();

        progressBar = view.findViewById(R.id.Ddo_ongoing_loading);
        recyclerView = view.findViewById(R.id.Ddo_ongoing_recyclerview);
        layoutManager = new LinearLayoutManager(getActivity());
        SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = prefs.getString("key", "");
        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        locationNames = new ArrayList<>();
        locationAddresses = new ArrayList<>();
        mAdoNames = new ArrayList<>();
        mIds = new ArrayList<>();
        auth_ado = new ArrayList<>();
        auth_ddo = new ArrayList<>();
        d_id = new ArrayList<>();
        adapter = new AdoDdoListAdapter(getActivity(), locationNames, locationAddresses, mAdoNames, mIds, auth_ado, auth_ddo, true, 2,false,true,false);
        recyclerView.setAdapter(adapter);
        //getData(mUrl);
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
        return view;
    }

    private void getData(final String url) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            if (resultsArray.length() == 0) {
                                //adapter.mshowshimmer = false;
                                adapter.notifyDataSetChanged();
                                nothing_toshow_fragment no_data = new nothing_toshow_fragment();
                                AppCompatActivity activity = (AppCompatActivity)getContext();
                                activity.getSupportFragmentManager().beginTransaction().replace(R.id.change_nodata_completed, no_data).commit();
                            }
                            Log.d("see here length: ","results array length: "+resultsArray.length());
                            for (int i = 0; i < resultsArray.length(); i++)
                            {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String id = singleObject.getString("id");
                                String a_ado, a_ddo;
                                mIds.add(id);
                                if (isDdo) {
                                    try {
                                        JSONObject adoObject = singleObject.getJSONObject("ado");
                                        JSONObject useronj_ado = adoObject.getJSONObject("user");
                                        String adoName = useronj_ado.getString("name");
                                        mAdoNames.add(adoName);
                                    } catch (JSONException e) {
                                        mAdoNames.add("Not Assigned");
                                    }
                                }
                                try {
                                    JSONObject adoobj = singleObject.getJSONObject("ado");
                                    JSONObject authado = adoobj.getJSONObject("user");
                                    a_ado = authado.getString("id");
                                    auth_ado.add(a_ado);
                                } catch (JSONException e) {
                                    a_ado = "null";
                                    auth_ado.add(a_ado);
                                }
                                try {
                                    JSONObject ddaobj = singleObject.getJSONObject("dda");
                                    JSONObject authddo = ddaobj.getJSONObject("user");
                                    a_ddo = authddo.getString("id");
                                    auth_ddo.add(a_ddo);
                                } catch (JSONException e) {
                                    a_ddo = "null";
                                    auth_ddo.add(a_ddo);
                                }
                                String locName = singleObject.getString("village_name") + ", " + singleObject.getString("block");
                                String locAdd = //singleObject.getString("block") + ", " +
                                        singleObject.getString("district") + ", " + singleObject.getString("state");
                                locationNames.add(locName);
                                locationAddresses.add(locAdd);
                            }
                            Log.d(TAG, "onResponse: NOTIFY " + mAdoNames + "    " + locationNames + "   " + locationAddresses);
                            adapter.notifyDataSetChanged();
                            // adapter.mshowshimmer = false;
                            spinner.setVisibility(View.GONE);
                            no_of_visits++;
                        } catch (JSONException e) {
                            spinner.setVisibility(View.GONE);
                            Toast.makeText(getActivity(),"An exception occurred",Toast.LENGTH_LONG).show();
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

                            final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                            View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
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
                                        Toast toast = Toast.makeText(getActivity(),"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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
                            Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ServerError) {
                            //Indicates that the server responded with a error response
                            Toast.makeText(getActivity(), "This error is server error", Toast.LENGTH_LONG).show();
                        } else if (error instanceof NetworkError) {
                            //Indicates that there was network error while performing the request
                            Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                        } else if (error instanceof ParseError) {
                            // Indicates that the server response could not be parsed
                            Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
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
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                                    adapter.notifyDataSetChanged();
                                    //todo image here
                                    nothing_toshow_fragment no_data = new nothing_toshow_fragment();
                                    AppCompatActivity activity = (AppCompatActivity)getContext();
                                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.change_nodata_completed, no_data).commit();
                                }
                                for (int i = 0; i < resultsArray.length(); i++)
                                {
                                    JSONObject singleObject = resultsArray.getJSONObject(i);
                                    String id = singleObject.getString("id");
                                    mIds.add(id);
                                    String a_ado, a_ddo;
                                    if (isDdo) {
                                        try {
                                            JSONObject adoObject = singleObject.getJSONObject("ado");
                                            JSONObject useronj_ado = adoObject.getJSONObject("user");
                                            String adoName = useronj_ado.getString("name");
                                            mAdoNames.add(adoName);
                                        } catch (JSONException e) {
                                            mAdoNames.add("Not Assigned");
                                        }
                                    }
                                    try {
                                        JSONObject adoobj = singleObject.getJSONObject("ado");
                                        JSONObject authado = adoobj.getJSONObject("user");
                                        a_ado = authado.getString("id");
                                        auth_ado.add(a_ado);
                                    } catch (JSONException e) {
                                        a_ado = "null";
                                        auth_ado.add(a_ado);
                                    }
                                    try {
                                        JSONObject ddaobj = singleObject.getJSONObject("dda");
                                        JSONObject authddo = ddaobj.getJSONObject("user");
                                        a_ddo = authddo.getString("id");
                                        auth_ddo.add(a_ddo);
                                    } catch (JSONException e) {
                                        a_ddo = "null";
                                        auth_ddo.add(a_ddo);
                                    }
                                    String locName = singleObject.getString("village_name") + ", " + singleObject.getString("block");
                                    String locAdd = //singleObject.getString("block") + ", " +
                                            singleObject.getString("district") + ", " + singleObject.getString("state");

//                                    String locName = singleObject.getString("village_name");
//                                    String locAdd = singleObject.getString("block") +
//                                            ", " + singleObject.getString("district");
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
                                final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                                View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
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
                                            Toast toast = Toast.makeText(getActivity(),"Tap on Close App again to exit app", Toast.LENGTH_LONG);
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
                                Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                                //Indicates that the server responded with a error response
                                Toast.makeText(getActivity(), "This error is server error", Toast.LENGTH_LONG).show();
                            } else if (error instanceof NetworkError) {
                                //Indicates that there was network error while performing the request
                                Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                // Indicates that the server response could not be parsed
                                Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
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
