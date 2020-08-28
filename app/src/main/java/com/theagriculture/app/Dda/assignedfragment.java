package com.theagriculture.app.Dda;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.theagriculture.app.Admin.Section;
import com.theagriculture.app.Admin.SectionAdapter;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class assignedfragment extends Fragment {

    private static final String TAG = "assignedfragment";
    private ArrayList<String> Id;
    private ArrayList<String> Name;
    private ArrayList<String> Address;
    private ArrayList<String> mAdoIds;
    private ArrayList<String> mDate;
    private DdapendingassignedAdapter ddaassignedAdapter;
    ArrayList<Section> sections = new ArrayList<>();
    private SectionAdapter recyclerViewAdater;
    private String urlget = "http://18.224.202.135/api/locations/dda/assigned";
    private String token;
    private String villagename;
    private String blockname;
    private String district;
    private String state;
    private String nextUrl;
    private boolean isNextBusy = false;
    private boolean isReferesh;
    private View view;
    private int length_of_arrray;
    private SwipeRefreshLayout swipeRefreshLayout;

    public assignedfragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ongoing,container,false);
        Id = new ArrayList<String>();
        Name = new ArrayList<String>();
        Address = new ArrayList<String>();
        mAdoIds = new ArrayList<>();
        mDate = new ArrayList<>();
        isReferesh = false;
        swipeRefreshLayout = view.findViewById(R.id.refreshpull_dda);
        RecyclerView review = view.findViewById(R.id.recyclerViewongoing);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.ongoing_dda_toolbar);
        toolbar.setVisibility(View.GONE);

        review.addOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int topRowVerticalPosition =
                        (recyclerView == null || recyclerView.getChildCount() == 0) ? 0 : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
                //swipeRefreshLayout.setRefreshing(false);

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(assignedfragment.this).attach(assignedfragment.this).commit();
            }
        });

        recyclerViewAdater = new SectionAdapter(getActivity(), sections,true);
        review.setAdapter(recyclerViewAdater);
//        recyclerViewAdater.notifyDataSetChanged();


//        ddaassignedAdapter = new DdapendingassignedAdapter(getActivity(), Id, Name, Address, mAdoIds,mDate);
//        review.setAdapter(ddaassignedAdapter);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        review.setLayoutManager(layoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(review.getContext(), layoutManager.getOrientation());
        review.addItemDecoration(divider);

        SharedPreferences preferences = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
        token = preferences.getString("token","");
        Log.d(TAG, "onCreateView: "+token);

        Log.d(TAG, "onCreateView: inflated fragment_ongoing");

        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlget, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
//                    String date = jsonObject.getString("created_on");
                    length_of_arrray = jsonArray.length();
                    if(length_of_arrray==0){
//                        ddaassignedAdapter.showassignedshimmer = false;
//                        ddaassignedAdapter.notifyDataSetChanged();
                        recyclerViewAdater.notifyDataSetChanged();
                        //todo image
                        view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                    }
                    for(int i=0;i<jsonArray.length();i++){
//                        sections = new ArrayList<>();
                        Id = new ArrayList<String>();
                        Name = new ArrayList<String>();
                        Address = new ArrayList<String>();
                        mAdoIds = new ArrayList<>();
//                        mDate = new ArrayList<>();
                        JSONObject singleObject = jsonArray.getJSONObject(i);
                        String date = singleObject.getString("created_on");
                        System.out.println(date);
                        Id.add(singleObject.getString("id"));
                        mDate.add(singleObject.getString("acq_date"));
                        villagename = singleObject.getString("village_name");
                        blockname = singleObject.getString("block_name");
                        district = singleObject.getString("district");
                        state = singleObject.getString("state");
                        Address.add(villagename.toUpperCase() + ", " + blockname.toUpperCase() +
                                ", " + district.toUpperCase());
                        JSONObject adoObject = singleObject.getJSONObject("ado");
                        Name.add(adoObject.getString("name"));
                        String adoId = adoObject.getString("id");
                        mAdoIds.add(adoId);
                        sections.add(new Section(date,Id,villagename,blockname,district,state,Address,Name,mAdoIds,true));
//                        recyclerViewAdater.notifyDataSetChanged();
//                        System.out.println("see here for size"+sections.size());
//                        System.out.println(sections);

                    }
//                    ddaassignedAdapter.showassignedshimmer = false;
//                    ddaassignedAdapter.notifyDataSetChanged();
                    recyclerViewAdater.notifyDataSetChanged();

                }catch (JSONException e){
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error );
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
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
        review.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = layoutManager.getItemCount();
                    pastItemCount = layoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = layoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getNextLocations();
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        isReferesh = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
        isReferesh = true;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
        if(isReferesh)
        {
            getFragmentManager().beginTransaction().detach(assignedfragment.this)
                    .attach(assignedfragment.this).commit();
            Log.d(TAG, "onResume: REFRESH");
            isReferesh = false;
        }
}

    private void getNextLocations() {
        final RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, nextUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(String.valueOf(response));
                    JSONArray jsonArray = jsonObject.getJSONArray("results");
                    nextUrl = jsonObject.getString("next");
                    length_of_arrray=jsonArray.length();
                    if(length_of_arrray==0){
//                        ddaassignedAdapter.showassignedshimmer = false;
//                        ddaassignedAdapter.notifyDataSetChanged();
                        recyclerViewAdater.notifyDataSetChanged();
                        //todo image
                        view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
//                        sections = new ArrayList<>();
                        Id = new ArrayList<String>();
                        Name = new ArrayList<String>();
                        Address = new ArrayList<String>();
                        mAdoIds = new ArrayList<>();
//                        mDate = new ArrayList<>();
                        JSONObject singleObject = jsonArray.getJSONObject(i);
                        String date = singleObject.getString("created_on");
                        mDate.add(singleObject.getString("acq_date"));
                        Id.add(singleObject.getString("id"));
                        villagename = singleObject.getString("village_name");
                        blockname = singleObject.getString("block_name");
                        district = singleObject.getString("district");
                        state = singleObject.getString("state");
                        Address.add(villagename.toUpperCase() + ", " + blockname.toUpperCase() +
                                ", " + district.toUpperCase());
                        JSONObject adoObject = singleObject.getJSONObject("ado");
                        Name.add(adoObject.getString("name"));
                        String adoId = adoObject.getString("id");
                        mAdoIds.add(adoId);
                        sections.add(new Section(date,Id,villagename,blockname,district,state,Address,Name,mAdoIds,true));
//                        recyclerViewAdater.notifyDataSetChanged();
//                        System.out.println(sections.size());
//                        System.out.println(sections);
                    }
                    isNextBusy = false;
//                    ddaassignedAdapter.showassignedshimmer = false;
//                    ddaassignedAdapter.notifyDataSetChanged();
                    recyclerViewAdater.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error);
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
