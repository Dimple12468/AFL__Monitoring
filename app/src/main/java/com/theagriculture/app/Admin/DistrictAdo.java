package com.theagriculture.app.Admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.theagriculture.app.Admin.AdoDdoActivity.nothing_toshow_fragment;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class DistrictAdo extends Fragment {
    private ArrayList<String> username;
    private ArrayList<String> userinfo;
    private ArrayList<String> mUserId;
    private ArrayList<String> mPkList;
    private ArrayList<String> mDdoNames;
    private ArrayList<String> mDistrictNames;
    private String ado_list;
    private String curr_dist;
    private String district_list_url;

    //private RecyclerViewAdater recyclerViewAdater;
    private DistrictAdoAdapter recyclerViewAdater;
    private String token;
    private String nextUrl;
    private ProgressBar progressBar;
    private LinearLayoutManager layoutManager;
    private GridLayoutManager gridlayout;
    private boolean isNextBusy = false;
    private RelativeLayout relativeLayout;
    private final String TAG = "ado_info_fragment";
    private RecyclerView Rview;
    private AlertDialog dialog;
    private TextView title;
    private ProgressBar spinner;
    boolean doubleBackToExitPressedOnce = false;

    ImageButton Ib,Ib1,Ib2,Ib3;
    TextView tv_edit;
    View v1,v2;
    Boolean is_settings_clicked = false;
    LinearLayout ll;

    //TextView title_top;
    String text;
    AdminActivity a;
    DistrictAdo d;

    public DistrictAdo() {
        // Required empty public constructor
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);
        MenuItem searchItem = menu.findItem(R.id.search_in_title);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search something");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                //customadapter.getFilter().filter(query);
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();

                }
                recyclerViewAdater.getFilter().filter(newText);
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_district_ado, container, false);
        final View view = inflater.inflate(R.layout.fragment_district_ado, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_district_ado);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        ll = view.findViewById(R.id.for_edit_ado);
        ado_list="";
        district_list_url ="http://18.224.202.135/api/district/";
        username = new ArrayList<>();
        userinfo = new ArrayList<>();
        mUserId = new ArrayList<>();
        mPkList = new ArrayList<>();
        mDdoNames = new ArrayList<>();
        mDistrictNames = new ArrayList<>();
        a = new AdminActivity();
        d = new DistrictAdo();

        tv_edit = view.findViewById(R.id.tv_edit);
        Ib = view.findViewById(R.id.ib_edit);
        Ib1 = view.findViewById(R.id.ib1_edit);
        Ib2 = view.findViewById(R.id.ib2_delete);
        Ib3 = view.findViewById(R.id.ib3_settings_fill);
        v1 = view.findViewById(R.id.view_edit);
        v2 = view.findViewById(R.id.vd1);

        Ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setVisibility(View.GONE);
                Ib.setVisibility(View.GONE);

                Ib1.setVisibility(View.VISIBLE);
                Ib2.setVisibility(View.VISIBLE);
                Ib3.setVisibility(View.VISIBLE);
                v2.setVisibility(View.VISIBLE);
                is_settings_clicked = true;
                DistrictAdoAdapter adapt = new DistrictAdoAdapter(getActivity(), username, userinfo, mUserId, false, mPkList, mDdoNames, mDistrictNames,is_settings_clicked);
                Rview.setAdapter(adapt);
                //RadioButton radioButton = view.findViewById(R.id.offer_select);
                //radioButton.setVisibility(View.VISIBLE);
            }
        });

        Ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Edit clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Delete clicked", Toast.LENGTH_SHORT).show();
            }
        });

        Ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v1.setVisibility(View.VISIBLE);
                Ib.setVisibility(View.VISIBLE);

                Ib1.setVisibility(View.GONE);
                Ib2.setVisibility(View.GONE);
                Ib3.setVisibility(View.GONE);
                v2.setVisibility(View.GONE);
                is_settings_clicked = false;

                DistrictAdoAdapter adapt = new DistrictAdoAdapter(getActivity(), username, userinfo, mUserId, false, mPkList, mDdoNames, mDistrictNames,is_settings_clicked);
                Rview.setAdapter(adapt);
            }
        });

        Bundle bundle = this.getArguments();
        curr_dist = bundle.getString("district");

       // title = view.findViewById(R.id.district_name);
        String low_title = curr_dist.toLowerCase();;
        int len=low_title.length();
        char one = curr_dist.charAt(0);
        String rest = low_title.substring(1,len);
        String whole= one + rest;
        //title.setText(whole+"'s ADO");
        text = whole+"'s ADO";
       // Char[] chars = low_title.toCharArray();
        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText(text);
        }else {
            title_top.setText("AFL Monitoring");
        }

        //title.setText(curr_dist+"'s ADO");

        spinner = view.findViewById(R.id.ado_progressbar);
        spinner.setVisibility(View.VISIBLE);

        progressBar = view.findViewById(R.id.ado_list_progressbar1);
        relativeLayout = view.findViewById(R.id.relativeLayout1);
        //recyclerViewAdater = new RecyclerViewAdater(getActivity(), username, userinfo, mUserId, false, mPkList, mDdoNames, mDistrictNames);
        recyclerViewAdater = new DistrictAdoAdapter(getActivity(), username, userinfo, mUserId, false, mPkList, mDdoNames, mDistrictNames);//is_settings_clicked);
        Rview = view.findViewById(R.id.recyclerViewado1);
        Rview.setAdapter(recyclerViewAdater);
        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        layoutManager = new LinearLayoutManager(getActivity());
        Rview.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), layoutManager.getOrientation());
        Rview.addItemDecoration(divider);
        //recyclerViewAdater.mShowShimmer = false;
        /*
        dialog = new SpotsDialog.Builder().setContext(getActivity()).setMessage("Loading...")
                .setTheme(R.style.CustomDialog)
                .setCancelable(false).build();

         */

        ado_list="http://18.224.202.135/api/users-list/ado/?search="+curr_dist;
        getData();
        /*
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ado_list, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();

                //relativeLayout.setBackground(getResources().getDrawable(R.drawable.data_background));
                //Log.d(TAG, "onResponse: sizes"+username.size()+userinfo.size());
                try {
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextUrl = rootObject.getString("next");
                    Log.d(TAG, "onResponse: " + nextUrl);
                    JSONArray resultsArray = rootObject.getJSONArray("results");
                    //Toast.makeText(getActivity(),"length of result array is "+resultsArray.length(),Toast.LENGTH_LONG).show();

                    if(resultsArray.length()== 0){
                        //recyclerViewAdater.mShowShimmer = false;
                        recyclerViewAdater.notifyDataSetChanged();
                        relativeLayout.setBackground(getResources().getDrawable(R.drawable.nothing_clipboard));
                        //relativeLayout.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                    }
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        username.add(singleObject.getString("name").toUpperCase());
                        JSONArray villageArray = singleObject.getJSONArray("village");
                        Log.d(TAG, "onResponse: LENGTH " + villageArray.length());
                        if (villageArray.length() == 0)
                            userinfo.add("NOT ASSIGNED");
                        for (int j = 0; j < 1; j++) {
                            try {
                                JSONObject villageObject = villageArray.getJSONObject(i);
                                userinfo.add(villageObject.getString("village").toUpperCase());
                            } catch (JSONException e) {
                                userinfo.add("NOT ASSIGNED");
                            }
                        }
                        JSONObject authObject = singleObject.getJSONObject("auth_user");
                        String pk = authObject.getString("pk");
                        mPkList.add(pk);
                        String id = singleObject.getString("id");
                        mUserId.add(id);
                        try {
                            JSONObject ddaObject = singleObject.getJSONObject("dda");
                            String ddaName = ddaObject.getString("name");
                            mDdoNames.add(ddaName);
                            try {
                                JSONObject districtObject = ddaObject.getJSONObject("district");
                                String districtName = districtObject.getString("district");
                                mDistrictNames.add(districtName.toUpperCase());
                            } catch (JSONException e) {
                                mDistrictNames.add("NOT ASSIGNED");
                            }
                        } catch (JSONException e) {
                            mDdoNames.add("Not Assigned");
                        }
                    }
                    //recyclerViewAdater.mShowShimmer = false;
                    recyclerViewAdater.notifyDataSetChanged();
                    spinner.setVisibility(View.GONE);
                    //dialog.dismiss();



                } catch (JSONException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onResponse: JSON" + e);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();

                } else if (error instanceof AuthFailureError) {
                    // Error indicating that there was an Authentication Failure while performing the request
                    Toast.makeText(getActivity(), "This error is case2", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    //Indicates that the server responded with a error response
                    Toast.makeText(getActivity(), "This error is case3", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

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
        Rview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + totalCount + " " + pastItemCount + " " + visibleItemCount);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextAdos();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

         */

        //getadolist(curr_dist);
        return view;
    }
    void getadolist(String district){


    }

    public void getData(){
        spinner.setVisibility(View.GONE);
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ado_list, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();

                //relativeLayout.setBackground(getResources().getDrawable(R.drawable.data_background));
                //Log.d(TAG, "onResponse: sizes"+username.size()+userinfo.size());
                try {
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextUrl = rootObject.getString("next");
                    Log.d(TAG, "onResponse: " + nextUrl);
                    JSONArray resultsArray = rootObject.getJSONArray("results");
                    //Toast.makeText(getActivity(),"length of result array is "+resultsArray.length(),Toast.LENGTH_LONG).show();

                    if(resultsArray.length()== 0){
                        //recyclerViewAdater.mShowShimmer = false;
                        ll.setVisibility(View.GONE);
                        recyclerViewAdater.notifyDataSetChanged();
                        //todo add image
                        nothing_toshow_fragment no_data = new nothing_toshow_fragment();
                        AppCompatActivity activity = (AppCompatActivity)getActivity();
                        activity.getSupportFragmentManager().beginTransaction().replace(R.id.nodata_dist_ado, no_data).commit();
                        //relativeLayout.setBackground(getResources().getDrawable(R.drawable.svg_nothing_toshow_1));
                        //relativeLayout.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                    }
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        username.add(singleObject.getString("name").toUpperCase());
                        JSONArray villageArray = singleObject.getJSONArray("village");
                        Log.d(TAG, "onResponse: LENGTH " + villageArray.length());
                        if (villageArray.length() == 0)
                            userinfo.add("NOT ASSIGNED");
                        for (int j = 0; j < 1; j++) {
                            try {
                                JSONObject villageObject = villageArray.getJSONObject(i);
                                userinfo.add(villageObject.getString("village").toUpperCase());
                            } catch (JSONException e) {
                                userinfo.add("NOT ASSIGNED");
                            }
                        }
                        JSONObject authObject = singleObject.getJSONObject("auth_user");
                        String pk = authObject.getString("pk");
                        mPkList.add(pk);
                        String id = singleObject.getString("id");
                        mUserId.add(id);
                        try {
                            JSONObject ddaObject = singleObject.getJSONObject("dda");
                            String ddaName = ddaObject.getString("name");
                            mDdoNames.add(ddaName);
                            try {
                                JSONObject districtObject = ddaObject.getJSONObject("district");
                                String districtName = districtObject.getString("district");
                                mDistrictNames.add(districtName.toUpperCase());
                            } catch (JSONException e) {
                                mDistrictNames.add("NOT ASSIGNED");
                            }
                        } catch (JSONException e) {
                            mDdoNames.add("Not Assigned");
                        }
                    }
                    //recyclerViewAdater.mShowShimmer = false;
                    recyclerViewAdater.notifyDataSetChanged();
                    recyclerViewAdater.show_suggestions(username);
                    spinner.setVisibility(View.GONE);
                    //dialog.dismiss();



                } catch (JSONException e) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                    Log.e(TAG, "onResponse: JSON" + e);
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                spinner.setVisibility(View.GONE);
                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    //This indicates that the reuest has either time out or there is no connection
                    //Toast.makeText(getActivity(), "This error is case1", Toast.LENGTH_LONG).show();
                    final BottomSheetDialog mBottomDialogNotificationAction = new BottomSheetDialog(getActivity());
                    View sheetView = getActivity().getLayoutInflater().inflate(R.layout.no_internet, null);
                    mBottomDialogNotificationAction.setContentView(sheetView);
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
                            getData();
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
                    Toast.makeText(getActivity(), "This is a server error", Toast.LENGTH_LONG).show();
                } else if (error instanceof NetworkError) {
                    //Indicates that there was network error while performing the request
                    Toast.makeText(getActivity(), "This error is case4", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    // Indicates that the server response could not be parsed
                    Toast.makeText(getActivity(), "This error is case5", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "An unknown error occurred.", Toast.LENGTH_SHORT).show();
                }
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

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
        Rview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {

                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + totalCount + " " + pastItemCount + " " + visibleItemCount);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextAdos();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void getNextAdos() {
       // Toast.makeText(getActivity(),"Loading next page",Toast.LENGTH_LONG).show();
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        Log.d(TAG, "getNextAdos: count ");
        progressBar.setVisibility(View.VISIBLE);
        final JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, nextUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject rootObject = new JSONObject(String.valueOf(response));
                    nextUrl = rootObject.getString("next");
                    JSONArray resultsArray = rootObject.getJSONArray("results");
                    for (int i = 0; i < resultsArray.length(); i++) {
                        JSONObject singleObject = resultsArray.getJSONObject(i);
                        username.add(singleObject.getString("name").toUpperCase());
                        JSONArray villageArray = singleObject.getJSONArray("village");
                        Log.d(TAG, "onResponse: LENGTH " + villageArray.length());
                        if (villageArray.length() == 0)
                            userinfo.add("NOT ASSIGNED");
                        for (int j = 0; j < 1; j++) {
                            try {
                                JSONObject villageObject = villageArray.getJSONObject(i);
                                userinfo.add(villageObject.getString("village").toUpperCase());
                            } catch (JSONException e) {
                                userinfo.add("NOT ASSIGNED");
                            }
                        }
                        JSONObject authObject = singleObject.getJSONObject("auth_user");
                        String pk = authObject.getString("pk");
                        mPkList.add(pk);
                        String id = singleObject.getString("id");
                        mUserId.add(id);
                        try {
                            JSONObject ddaObject = singleObject.getJSONObject("dda");
                            String ddaName = ddaObject.getString("name");
                            mDdoNames.add(ddaName);
                            try {
                                JSONObject districtObject = ddaObject.getJSONObject("district");
                                String districtName = districtObject.getString("district");
                                mDistrictNames.add(districtName.toUpperCase());
                            } catch (JSONException e) {
                                mDistrictNames.add("NOT ASSIGNED");
                            }
                        } catch (JSONException e) {
                            mDdoNames.add("Not Assigned");
                        }
                    }
                    Log.d(TAG, "onResponse: " + username);
                    recyclerViewAdater.notifyDataSetChanged();
                    isNextBusy = false;

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NoConnectionError)
                    Toast.makeText(getActivity(), "Check Your Internet Connection Please!", Toast.LENGTH_SHORT).show();
                isNextBusy = false;
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + token);
                return map;
            }
        };

        requestQueue.add(jsonArrayRequest);
        requestFinished(requestQueue);
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

    }

    @Override
    public void onPause() {
        spinner.setVisibility(View.GONE);
        super.onPause();
    }

   /* @Override
    public void onResume() {
        spinner.setVisibility(View.GONE);
        super.onResume();
    }*/

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    public String for_title_top(/*TextView top_heading,int id*/){
        /*if (d.isVisible()) {
            System.out.println("Dimple districtddo ambal wala text");
            top_heading.setText(text);
            //return text;
        }else{
            top_heading.setText("AFL Monitoring");
        }*/
        return text;
    }


}
