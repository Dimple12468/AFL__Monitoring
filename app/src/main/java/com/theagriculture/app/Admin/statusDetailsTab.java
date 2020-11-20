package com.theagriculture.app.Admin;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.Globals;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class statusDetailsTab {

    private String pendingUrl = Globals.pendingDatewiseList;
    private String nextUrl = "null";
    private static final String TAG = "statusDetailsTab";
    private String key;
    private ArrayList<String> allDates, Dates_s;// = new ArrayList<>();
    private Context Context;
    private JsonObjectRequest jsonObjectRequest;
    private JSONObject jsonObject,singleObject,adoObj,adoUser,ddaObj,ddaUser,villageName,districtName;
    private JSONArray jsonArray;
    private String adoName = "null",ddaName = "null",address = "null";
    private ArrayList<String> addressList,adoNameList,ddaNameList;
    private int count = 0;
    private ArrayList<Section> sections = new ArrayList<>();
    private SectionAdapter recyclerViewAdater;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;


    public void getData(String url, final Context context) {
        Context = context;
        final SharedPreferences preferences = Context.getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        key = preferences.getString("key", "");
        Log.d(TAG, "key: " + key);

        final RequestQueue requestQueue = Volley.newRequestQueue(Context);
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    jsonObject = new JSONObject(String.valueOf(response));
                    nextUrl = jsonObject.getString("next");
                    Log.d(TAG, "onResponse: nextUrl " + nextUrl);
                    jsonArray = jsonObject.getJSONArray("results");

                    allDates = new ArrayList<>();
                    addressList = new ArrayList<>();
                    adoNameList = new ArrayList<>();
                    ddaNameList = new ArrayList<>();

                    if (count==0) {
                        count++;
                        JSONObject singleObjectInitial = jsonArray.getJSONObject(0);
                        allDates.add(singleObjectInitial.getString("acq_date"));

                        String village_Name,district_Name;

                        try {
                            villageName = singleObjectInitial.getJSONObject("village_name");
                            village_Name  = villageName.getString("village");
                        } catch (JSONException e) {
                            village_Name = "NULL";
                        }

                        try {
                            districtName = singleObjectInitial.getJSONObject("district");
                            district_Name = districtName.getString("district");
                        } catch (JSONException e) {
                            district_Name = "NULL";
                        }

                        address = village_Name.toUpperCase() + ", " + singleObjectInitial.getString("block").toUpperCase() + ", " +
                                district_Name.toUpperCase() ;

//                        address = singleObject.getJSONObject("village_name").getString("village").toUpperCase() + "," + singleObject.getString("block").toUpperCase() + "," +
//                                singleObject.getJSONObject("district").getString("district").toUpperCase();

                        try {
                            adoObj = singleObjectInitial.getJSONObject("ado");
                            adoUser = adoObj.getJSONObject("user");
                            adoName = adoUser.getString("name");
                        } catch (JSONException e) {
                            adoName = "Not Assigned";
                        }

                        try {
                            ddaObj = singleObjectInitial.getJSONObject("dda");
                            ddaUser = ddaObj.getJSONObject("user");
                            ddaName = ddaUser.getString("name");
                        } catch (JSONException e) {
                            ddaName = "Not Assigned";
                        }

                        addressList.add(address);
                        adoNameList.add(adoName);
                        ddaNameList.add(ddaName);

                        Log.d(TAG,"date in count: " + allDates);
                        Log.d(TAG,"address in count: " + addressList);
                        Log.d(TAG,"ado name in count: " + adoNameList);
                        Log.d(TAG,"dda name in count: " + ddaNameList);
                    }

                    for (int i=1;i<jsonArray.length();i++){
                        singleObject = jsonArray.getJSONObject(i);

                        allDates.add(singleObject.getString("acq_date"));

                        String village_Name,district_Name;

                        try {
                            villageName = singleObject.getJSONObject("village_name");
                            village_Name  = villageName.getString("village");
                        } catch (JSONException e) {
                            village_Name = "NULL";
                        }

                        try {
                            districtName = singleObject.getJSONObject("district");
                            district_Name = districtName.getString("district");
                        } catch (JSONException e) {
                            district_Name = "NULL";
                        }

                        address = village_Name.toUpperCase() + ", " + singleObject.getString("block").toUpperCase() + ", " +
                                district_Name.toUpperCase() ;

//                        address = singleObject.getJSONObject("village_name").getString("village").toUpperCase() + "," + singleObject.getString("block").toUpperCase() + "," +
//                                singleObject.getJSONObject("district").getString("district").toUpperCase();

                        try {
                            adoObj = singleObject.getJSONObject("ado");
                            adoUser = adoObj.getJSONObject("user");
                            adoName = adoUser.getString("name");
                        } catch (JSONException e) {
                            adoName = "Not Assigned";
                        }

                        try {
                            ddaObj = singleObject.getJSONObject("dda");
                            ddaUser = ddaObj.getJSONObject("user");
                            ddaName = ddaUser.getString("name");
                        } catch (JSONException e) {
                            ddaName = "Not Assigned";
                        }

                        if (allDates.get(i - 1).equals(allDates.get(i))){
                            String d_date = allDates.get(i-1);
                            addressList.add(address);
                            adoNameList.add(adoName);
                            ddaNameList.add(ddaName);
                            Dates_s = new ArrayList<>();
                            Dates_s.add(d_date);

                            Log.d(TAG,"date: " + Dates_s);
                            Log.d(TAG,"address: " + addressList);
                            Log.d(TAG,"ado name: " + adoNameList);
                            Log.d(TAG,"dda name: " + ddaNameList);
                            sections.add(new Section(Dates_s,addressList,adoNameList,ddaNameList,true,false,false));
                        }else{
                            String d_date = allDates.get(i - 1);

//                            allDates = new ArrayList<>();
                            Dates_s = new ArrayList<>();
                            addressList = new ArrayList<>();
                            adoNameList = new ArrayList<>();
                            ddaNameList = new ArrayList<>();

//                            allDates.add(d_date);
                            Dates_s.add(d_date);
                            addressList.add(address);
                            adoNameList.add(adoName);
                            ddaNameList.add(ddaName);

                            Log.d(TAG,"date in else: " + Dates_s);
                            Log.d(TAG,"address in else: " + addressList);
                            Log.d(TAG,"ado name in else: " + adoNameList);
                            Log.d(TAG,"dda name in else: " + ddaNameList);
                            sections.add(new Section(Dates_s,addressList,adoNameList,ddaNameList,true,false,false));
                        }
                        recyclerViewAdater.notifyDataSetChanged();
                    }

//                    if (!nextUrl.equals("null")) {
//                        String nextUrlWithS = "https" + nextUrl.substring(4);
//                        Log.d(TAG, "Next URL with s: " + nextUrlWithS);
//                        getData(nextUrlWithS, Context);
//                    }
//                    else {
//                        collectData();
//                    }

                } catch (Exception e) {
                    Toast.makeText(Context, "An exception occurred", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Token " + key);
                return map;
            }
        };

        requestQueue.add(jsonObjectRequest);
        requestFinished(requestQueue);//this function is defined below
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

    private void requestFinished(RequestQueue queue) {

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                progressBar.setVisibility(View.GONE);
//                spinner.setVisibility(View.GONE);
            }
        });

    }

    public void collectData(){
//        for (int i = 0;i<jsonArray.length();i++){
//            JSONObject singleObject = jsonArray.getJSONObject(i);
//        }
        for (int i=0;i<allDates.size();i++){
            Log.d(TAG,"date at index " + i + ": "+ allDates.get(i) + " ");
//            Log.d(TAG,"address " )
        }
    }

    public void getAdapter(Context context, int recyclerViewpending) {
        recyclerViewAdater = new SectionAdapter(context, sections);
        recyclerView.setAdapter(recyclerViewAdater);
        recyclerViewAdater.notifyDataSetChanged();
    }

    public void passComponents(RecyclerView recyclerView, ProgressBar spinner) {
//        this.recyclerView = recyclerView;
//        this.progressBar = spinner;
        this.recyclerView =
    }
}
