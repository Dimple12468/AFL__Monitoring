package com.theagriculture.app.Dda;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.theagriculture.app.R;
import com.theagriculture.app.villageNameFragmentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class villageNameFragment extends Fragment {
    private static String TAG = "villagenameActivity";
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
    //private VillagesUnderDistrictAdapter adapter;
    villageNameFragmentAdapter adapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ProgressBar listNextProgressBar;
    SparseBooleanArray villagesIdSparse;// = adapter.getSparseBooleanArray();
    ArrayList<Integer> selectedIds = new ArrayList<>();

    public villageNameFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_village_name, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.app__bar_dda_village);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);

        //for title heading
        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Ado list");
        }else {
            title_top.setText("AFL Monitoring");
        }

        recyclerView = view.findViewById(R.id.chk_recycler_dda);
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_tick_chk_dda);
        progressBar = (ProgressBar) view.findViewById(R.id.village_name_loading);
        listNextProgressBar = view.findViewById(R.id.village_name_loadingnext);


        villagesNames = new ArrayList<>();
        villageIds = new ArrayList<>();
        suggestedVillageNames = new ArrayList<>();
        suggestedVillageIds = new ArrayList<>();

        Bundle bundle = this.getArguments();
        String districtid = bundle.get("districtId").toString();
        mAdoId = bundle.get("adoId").toString();
        String currentVillages = bundle.get("currentVillages").toString();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(),"You clicked fab",Toast.LENGTH_LONG).show();
                /*
                name= new StringBuffer();
                id = new StringBuffer();
                for(String p : adapter.checkedVillageName){
                    name.append(p);
                    name.append("\n");
                }

                for(Integer p:adapter.checkedVillageId){
                    id.append(p);
                    id.append("\n");
                }


                //Toast.makeText(getActivity(),name.toString()+id.toString(),Toast.LENGTH_LONG).show();

                if(adapter.checkedVillageName.size()>0){
                    Toast.makeText(getActivity(),name.toString()+id.toString(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getActivity(),"Nothing selected ",Toast.LENGTH_LONG).show();
                }

                 */
                for(Integer p:adapter.checkedVillageId){
                    selectedIds.add(p);
                }
                //Toast.makeText(getActivity(),selectedIds.toString(),Toast.LENGTH_LONG).show();
                saveChanges();

            }
        });


        mUrl = "http://api.theagriculture.tk/api/villages-list/district/" + districtid + "/";

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token", "");
        progressBar.setVisibility(View.VISIBLE);
        loadData(mUrl);

        layoutManager = new LinearLayoutManager(getActivity());
        //adapter = new VillagesUnderDistrictAdapter(getActivity(), villagesNames, villageIds);
        adapter = new villageNameFragmentAdapter(getActivity(),villagesNames,villageIds);

        adapter.notifyDataSetChanged();
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        return view;
    }

    private void loadData(String url) {
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
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
                            Toast.makeText(getActivity(), "Please try again", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: JSON EXCEPTION " + e);
                            isNextBusy = false;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof NoConnectionError)
                            Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(getActivity(), "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onErrorResponse: loadData " + error);
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
                            listNextProgressBar.setVisibility(View.VISIBLE);
                            loadData(nextUrl);
                        }
                        Log.d(TAG, "onScrolled:");
                    }
                    Log.d(TAG, "onScrolled: " + totalCount + "total" + pastCount + "past" + visibleCount + "visible");
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(jsonObjectRequest);
    }

    private void saveChanges() {
        //if (villagesIdSparse.size() != 0) {
        if(selectedIds.size()!=0){
            /*
            ArrayList<Integer> selectedIds = new ArrayList<>();
            for (int i = 0; i < villagesNames.size(); i++) {
                boolean isSelectedVillage = villagesIdSparse.get(villageIds.get(i));
                if (isSelectedVillage)
                    selectedIds.add(villageIds.get(i));
            }
            Log.d(TAG, "saveChanges: " + villageIds);
            Log.d(TAG, "saveChanges: " + selectedIds);

         */
            JSONArray villagesIdArray = new JSONArray(selectedIds);
            JSONObject paramObject = new JSONObject();
            try {
                paramObject.put("village", villagesIdArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "saveChanges: " + paramObject);
            String url = "http://api.theagriculture.tk/api/user/" + mAdoId + "/";
            JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.PUT, url, paramObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                JSONObject jsonObject = new JSONObject(String.valueOf(response));
                                Log.d(TAG, "onResponse: " + jsonObject);
                                Toast.makeText(getActivity(), "Villages Successfully Assigned!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getActivity(), DdaActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("isAssignedLocation", true);
                                startActivity(intent);
                                //finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "onResponse: JSON EXCEPTION " + e);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError)
                                Toast.makeText(getActivity(), "Please check your Internet Connection!", Toast.LENGTH_LONG).show();
                            else
                                Toast.makeText(getActivity(), "Something went wrong, please try again!", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onErrorResponse: loadData " + error);
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
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
            Toast.makeText(getActivity(), "No village Selected", Toast.LENGTH_SHORT).show();
    }


}
