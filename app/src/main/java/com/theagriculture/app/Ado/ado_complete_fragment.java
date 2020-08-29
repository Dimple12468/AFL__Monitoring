package com.theagriculture.app.Ado;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.theagriculture.app.Admin.Section;
import com.theagriculture.app.Admin.SectionAdapter;
import com.theagriculture.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.nfc.tech.MifareUltralight.PAGE_SIZE;

public class ado_complete_fragment extends Fragment {

    private ArrayList<String> mtextview1;
    private ArrayList<String> mtextview2;
    private RecyclerView recyclerView;
    private SectionAdapter_ado adoListAdapter;
    private ArrayList<String> longitude;
    private ArrayList<String> latitude;
    private String url = "http://api.theagriculture.tk/api/locations/ado/completed";
    private String nextUrl;
    private boolean isNextBusy = false;
    private View view;
    private ArrayList<String> mId;
    private SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<Section_ado> sections = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    ProgressBar spinner;

    MenuItem searchItem;
    MenuItem searchItem_filter;

    //tag
    private final String TAG = "ado_complete_fragmnt";

    public ado_complete_fragment(){

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_top_bar,menu);

        searchItem = menu.findItem(R.id.search_in_title);
        searchItem_filter = menu.findItem(R.id.filter);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search something");

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                searchItem_filter.setVisible(true);
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                searchItem_filter.setVisible(false);
                return true;
            }
        });
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
                /*
                if (newText.equals("")) {
                    //searchView.setQuery("", false);
                    newText = newText.trim();

                }
                //customadapter.getFilter().filter(newText);

                 */
                return true;
            }
        });
        searchItem_filter.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // alert_filter_dialog();
                return true;
            }


        });


        // MenuItemCompat.setOn
        /*searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                //searchItem_filter.setVisible(false);
               // if (getSupportActionBar().setDisplayHomeAsUpEnabled(true))


                searchItem_filter.setVisible(false);
                dialog.invalidateOptionsMenu();
                return false;
            }
        });*/

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ado_complete_fragment, container, false);
        mtextview1 = new ArrayList<>();
        mtextview2 = new ArrayList<>();
        longitude = new ArrayList<>();
        latitude = new ArrayList<>();
        mId = new ArrayList<>();
        //add data in the array with load data

        Toolbar toolbar = view.findViewById(R.id.app__bar_ado_completed);
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(toolbar);
        appCompatActivity.getSupportActionBar().setDisplayShowTitleEnabled(false);

        TextView title_top = view.findViewById(R.id.app_name);
        if (view.isEnabled()){
            title_top.setText("Completed");
        }else {
            title_top.setText("AFL Monitoring");
        }

        final ActionBar actionBar =((AppCompatActivity) getActivity()).getSupportActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);//to display search icon

        Log.d(TAG, "onCreateView: inside onCreate");


        recyclerView = view.findViewById(R.id.ado_completed_rv);
        spinner= view.findViewById(R.id.ado_complete_progress);
        spinner.setVisibility(View.VISIBLE);
        swipeRefreshLayout = view.findViewById(R.id.refreshpull8);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFragmentManager().beginTransaction().detach(ado_complete_fragment.this).attach(ado_complete_fragment.this).commit();
            }
        });


        /*//////////////// working but second time
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        recyclerView.setAdapter(adoListAdapter);
        adoListAdapter.notifyDataSetChanged();
        getData(url);

         */
        /*
        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        getData(url);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        //DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),linearLayoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);



        recyclerView.setAdapter(adoListAdapter);
        recyclerView.setHasFixedSize(true);
        adoListAdapter.notifyDataSetChanged();
        //adoListAdapter.notifyDataSetChanged();

         */

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);

        /////////////////////////////
        getData(url);
        adoListAdapter = new SectionAdapter_ado(getActivity(),sections);
        recyclerView.setAdapter(adoListAdapter);
        adoListAdapter.notifyDataSetChanged();

        //getData(url);
        /*
        sections = new ArrayList<>();
        Log.d(TAG, "getData: inside getdata");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            Log.d(TAG, "onResponse1: ");
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            Log.d(TAG, "onResponse2: ");
                            if(resultsArray.length()== 0){
                                //adoListAdapter.mshowshimmer = false;
                                adoListAdapter.notifyDataSetChanged();
                                Log.d(TAG, "onResponse3: ");
                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }


                            String[][] arr = new String[6][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                String dlocation_name = singleObject.getString("village_name");
                                String dlocation_address = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("district");
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                arr[0][i]=ddate;
                                arr[1][i]=did;
                                arr[2][i]=dlocation_name;
                                arr[3][i]=dlocation_address;
                                arr[4][i]=dlatitude;
                                arr[5][i]=dlongitude;
                            }
                            Log.d(TAG, "onResponse4: ");
                            String inter;
                            for(int i=0;i<resultsArray.length()-1;i++){
                                for(int j=0;j<resultsArray.length()-i-1;j++){
                                    String idate = arr[0][j];
                                    String ndate = arr[0][j+1];
                                    SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
                                    // Get the two dates to be compared
                                    Date d1 = null;
                                    try {
                                        d1 = sdfo.parse(idate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date d2 = null;
                                    try {
                                        d2 = sdfo.parse(ndate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (d1.compareTo(d2) < 0) {
                                        for(int k=0;k<6;k++) {
                                            inter = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = inter;
                                        }
                                    }
                                }
                            }
                            Log.d(TAG, "onResponse5: ");
                            ArrayList<String> mDid = new ArrayList<>();
                            ArrayList<String> mDlocation_name = new ArrayList<>();
                            ArrayList<String> mDlocation_address = new ArrayList<>();
                            ArrayList<String> mlatitude= new ArrayList<>();
                            ArrayList<String> mlongitude= new ArrayList<>();
                            String predate=arr[0][0];
                            //String predate = null;
                            for(int i=0;i<resultsArray.length();i++){
                                String idate = arr[0][i];
                                if(predate.equals(idate)){
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);

                                    //predate=idate;
                                }
                                else{
                                    sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude));
                                    mDid = new ArrayList<>();
                                    mDlocation_name = new ArrayList<>();
                                    mDlocation_address = new ArrayList<>();
                                    mlatitude = new ArrayList<>();
                                    mlongitude = new ArrayList<>();
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);
                                    //date.equals(idate);
                                }
                                //predate.equals(idate);
                                predate=idate;
                            }
                            Log.d(TAG, "onResponse6: ");
                            sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude));
                            Log.d(TAG, "onResponse7: ");
                            adoListAdapter.notifyDataSetChanged();
                            isNextBusy = false;
                            spinner.setVisibility(View.GONE);
                            Log.d(TAG, "onResponse8: ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: inside the evception" + e);
                            spinner.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: inside the the error exception" + error);
                        if(error instanceof NoConnectionError || error instanceof TimeoutError){
                            Toast.makeText(getActivity(), "This error is no internet", Toast.LENGTH_LONG).show();
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
                        spinner.setVisibility(View.GONE);
                        isNextBusy = false;
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("token", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        Log.d(TAG, "onResponse9: ");
        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, "onResponse10: ");
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = linearLayoutManager.getItemCount();
                    pastItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getData(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        Log.d(TAG, "onResponse after getDate: ");
        */


        /*

        adoListAdapter = new AdoListAdapter(getContext(), mtextview1, mtextview2, true, mId);
        recyclerView.setAdapter(adoListAdapter);

         */


        return view;
    }


    private void getData(String url) {
        spinner.setVisibility(View.VISIBLE);
        sections = new ArrayList<>();
        Log.d(TAG, "getData: inside getdata");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        isNextBusy = true;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_LONG).show();
                        try {
                            Log.d(TAG, "onResponse1: ");
                            JSONObject rootObject = new JSONObject(String.valueOf(response));
                            //Toast.makeText(getActivity(),rootObject.toString(),Toast.LENGTH_LONG).show();
                            nextUrl = rootObject.getString("next");
                            JSONArray resultsArray = rootObject.getJSONArray("results");
                            Log.d(TAG, "onResponse2: ");
                            if(resultsArray.length()== 0){
                                //adoListAdapter.mshowshimmer = false;
                                adoListAdapter.notifyDataSetChanged();
                                Log.d(TAG, "onResponse3: ");
                                view.setBackground(getActivity().getResources().getDrawable(R.mipmap.no_entry_background));
                                //view.getView().setBackground(getActivity().getResources().getDrawable(R.drawable.no_entry_background));
                            }
                            /*
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String id = singleObject.getString("id");
                                mId.add(id);
                                String location_name = singleObject.getString("village_name");
                                String location_address = singleObject.getString("block_name") + ", "
                                        + singleObject.getString("district");
                                String slongitude = singleObject.getString("longitude");
                                String slatitude = singleObject.getString("latitude");
                                mtextview1.add(location_name.toUpperCase());
                                mtextview2.add(location_address.toUpperCase());
                                longitude.add(slongitude);
                                latitude.add(slatitude);
                            }
                            adoListAdapter.sendPostion(longitude, latitude);

                             */

                            String[][] arr = new String[6][resultsArray.length()];
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject singleObject = resultsArray.getJSONObject(i);
                                String did = singleObject.getString("id");
                                String dlocation_name = singleObject.getString("village_name");
                                String dlocation_address = singleObject.getString("block_name") + ", " +
                                        singleObject.getString("district");
                                String dlongitude = singleObject.getString("longitude");
                                String dlatitude = singleObject.getString("latitude");
                                String ddate = singleObject.getString("acq_date");
                                arr[0][i]=ddate;
                                arr[1][i]=did;
                                arr[2][i]=dlocation_name;
                                arr[3][i]=dlocation_address;
                                arr[4][i]=dlatitude;
                                arr[5][i]=dlongitude;
                            }
                            Log.d(TAG, "onResponse4: ");
                            String inter;
                            for(int i=0;i<resultsArray.length()-1;i++){
                                for(int j=0;j<resultsArray.length()-i-1;j++){
                                    String idate = arr[0][j];
                                    String ndate = arr[0][j+1];
                                    SimpleDateFormat sdfo = new SimpleDateFormat("yyyy-MM-dd");
                                    // Get the two dates to be compared
                                    Date d1 = null;
                                    try {
                                        d1 = sdfo.parse(idate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    Date d2 = null;
                                    try {
                                        d2 = sdfo.parse(ndate);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (d1.compareTo(d2) < 0) {
                                        for(int k=0;k<6;k++) {
                                            inter = arr[k][j];
                                            arr[k][j] = arr[k][j + 1];
                                            arr[k][j + 1] = inter;
                                        }
                                    }
                                }
                            }
                            Log.d(TAG, "onResponse5: ");
                            ArrayList<String> mDid = new ArrayList<>();
                            ArrayList<String> mDlocation_name = new ArrayList<>();
                            ArrayList<String> mDlocation_address = new ArrayList<>();
                            ArrayList<String> mlatitude= new ArrayList<>();
                            ArrayList<String> mlongitude= new ArrayList<>();
                            String predate=arr[0][0];
                            //String predate = null;
                            for(int i=0;i<resultsArray.length();i++){
                                String idate = arr[0][i];
                                if(predate.equals(idate)){
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);

                                    //predate=idate;
                                }
                                else{
                                    sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,false,true));
                                    mDid = new ArrayList<>();
                                    mDlocation_name = new ArrayList<>();
                                    mDlocation_address = new ArrayList<>();
                                    mlatitude = new ArrayList<>();
                                    mlongitude = new ArrayList<>();
                                    mDid.add(arr[1][i]);
                                    mDlocation_name.add(arr[2][i]);
                                    mDlocation_address.add(arr[3][i]);
                                    mlatitude.add(arr[4][i]);
                                    mlongitude.add(arr[5][i]);
                                    //date.equals(idate);
                                }
                                //predate.equals(idate);
                                predate=idate;
                            }
                            Log.d(TAG, "onResponse6: ");
                            sections.add(new Section_ado(predate,mDid, mDlocation_name, mDlocation_address,mlatitude,mlongitude,false,true));
                            Log.d(TAG, "onResponse7: ");
                            //Toast.makeText(getActivity(),arr.toString(),Toast.LENGTH_LONG).show();
                            /*
                            String abc = "";
                            for(int i=0;i<resultsArray.length();i++){
                                for(int j=0;j<6;j++){
                                    abc= abc+ " "+arr[j][i];
                                }
                                abc= abc+ ", ";
                            }
                            Toast.makeText(getActivity(),abc,Toast.LENGTH_LONG).show();
                            //adoListAdapter.mshowshimmer = false;
                            adoListAdapter.notifyDataSetChanged();

                             */
                            adoListAdapter.notifyDataSetChanged();
                            isNextBusy = false;
                            spinner.setVisibility(View.GONE);
                            Log.d(TAG, "onResponse8: ");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),"An exception occured",Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: inside the evception" + e);
                            spinner.setVisibility(View.GONE);

                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: inside the the error exception" + error);
                        if(error instanceof NoConnectionError || error instanceof TimeoutError){
                            Toast.makeText(getActivity(), "This error is no internet", Toast.LENGTH_LONG).show();
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
                        spinner.setVisibility(View.GONE);
                        isNextBusy = false;
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                SharedPreferences prefs = getActivity().getSharedPreferences("tokenFile", Context.MODE_PRIVATE);
                String token = prefs.getString("token", "");
                map.put("Authorization", "Token " + token);
                return map;
            }
        };
        Log.d(TAG, "onResponse9: ");
        requestQueue.add(jsonObjectRequest);
        Log.d(TAG, "onResponse10: ");
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
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                int totalCount, pastItemCount, visibleItemCount;
                if (dy > 0) {
                    totalCount = linearLayoutManager.getItemCount();
                    pastItemCount = linearLayoutManager.findFirstVisibleItemPosition();
                    visibleItemCount = linearLayoutManager.getChildCount();
                    if ((pastItemCount + visibleItemCount) >= totalCount) {
                        Log.d(TAG, "onScrolled: " + nextUrl);
                        if (!nextUrl.equals("null") && !isNextBusy)
                            getData(nextUrl);
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        /*
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            int totalCount, pastItemCount, visibleItemCount;
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: out DX " + dx + " DY " + dy);
            }
        });

         */
    }
}